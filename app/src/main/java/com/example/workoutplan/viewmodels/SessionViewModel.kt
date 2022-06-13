package com.example.workoutplan.viewmodels

import android.os.Build
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.data.relations.SessionItem
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.LongFunction
import kotlin.math.abs
import kotlin.math.roundToInt

class SessionViewModel(
    workoutExeRepository: WorkoutExerciseCrossRefRepository,
    private val sessionRepository: SessionRepository,
    workoutId: Long): ViewModel() {

    val exerciseDetailed = workoutExeRepository.getExercisesDetailedByWorkoutId(workoutId)
    /**
     * Link to the actual exercises
     */
    private val _actualExerciseId = MutableLiveData(0L)
    val actualExerciseId: LiveData<Long>
        get() = _actualExerciseId

    /**
     * Link to the next exercises
     */
    private var _nextExerciseId: MutableLiveData<Long?> = MutableLiveData(0L)
    val nextExerciseId: LiveData<Long?>
        get() = _nextExerciseId

    private lateinit var exercisesPool: MutableList<Long>

    private var startTime = System.currentTimeMillis()

    /**
     * Object used to iterate the workout session and show on View
     */
    val sessionWorkout = Transformations.switchMap(exerciseDetailed) {
        when(it) {
            null -> null
            else -> {
                _actualExerciseId.value = it.first().exerciseId
                _nextExerciseId.value = it[1].exerciseId
                transformToSessionWorkout(it)
            }
        }
    }

    /**
     * Link to the previous exercises
     */
    private var _previousExerciseId: MutableLiveData<Long?> = MutableLiveData(null)
    val previousExerciseId: LiveData<Long?>
        get() = _previousExerciseId

    /**
     * Link to the sessionItem to Edit
     */
    private val _sessionItemToEdit: MutableLiveData<SessionItem?> = MutableLiveData(null)
    val sessionItemToEdit: LiveData<SessionItem?>
        get() = _sessionItemToEdit
    /**
     * Link to the sessionItem to edit Position
     */
    private var _sessionItemToEditPosition: Int? = null

    /**
     * Link to the time left
     */
    private val _timeLeft: MutableLiveData<Long> = MutableLiveData()
    val timeLeft: LiveData<Long>
        get() = _timeLeft

    /**
     * Livedata to navigate to SummaryPage
     */
    private val _navigateToSummaryPage = MutableLiveData<Boolean?>()
    val navigateToSummaryPage: LiveData<Boolean?>
        get() = _navigateToSummaryPage

    /**
     * Livedata to navigate to Exercise Page
     */
    private val _navigateToWorkoutExercisePage = MutableLiveData<Long?>()
    val navigateToWorkoutExercisePage : LiveData<Long?>
        get() = _navigateToWorkoutExercisePage

    /**
     * Livedata use to control the countdown
     */
    private val _state: MutableLiveData<TimerState> = MutableLiveData(TimerState.END)
    val state: LiveData<TimerState>
        get() = _state

    var timeTarget: Long = 0L

    private lateinit var countDownTimer: CountDownTimer

    fun navigateToSummaryPage(session: Session) {
        _navigateToSummaryPage.value = true
    }

    fun navigateToSummaryPageDone() {
        _navigateToSummaryPage.value = null
    }

    fun onNavigateToWorkoutExercisePage(exerciseId: Long) {
        _navigateToWorkoutExercisePage.value = exerciseId
    }

    fun navigateToWorkoutExercisePageDone() {
        _navigateToWorkoutExercisePage.value = null
    }

    fun toggleButton() {
        if (state.value != TimerState.RUNNING) {
            this.startCountDown()
        } else {
            this.pauseCountDown()
        }
        Log.d(TAG,"toggleButton() - Status = ${_state.value}")
    }

    fun nextExercise() {
        if(_state.value == TimerState.END) {
            return
        }
        if(_nextExerciseId.value != null) {
            val nextExeId = exercisesPool.getOrNull(exercisesPool.indexOf(
                _nextExerciseId.value)+1)

            getAllSessionItemRunningAvailable()?.forEach {
                it.status = SessionItem.Companion.STATUS.UNDONE }

            _previousExerciseId.value = actualExerciseId.value
            _actualExerciseId.value = _nextExerciseId.value
            _nextExerciseId.value = nextExeId
            Log.d(TAG,"nextExercise() - Status ${_state.value}, PreviousExeId = ${_previousExerciseId.value}, ActualExeId =" +
                    " ${_actualExerciseId.value}, NextExeId = ${_nextExerciseId.value} ")

            this.stopCountDown()

        } else {
            this.endCountDown()
        }
    }

    fun prevExercise() {
        if(_state.value == TimerState.END) {
            return
        }

        if(_previousExerciseId.value != null) {
            val prevExeId = exercisesPool.getOrNull(exercisesPool.indexOf(
                _previousExerciseId.value)-1)

            getAllSessionItemRunningAvailable()?.forEach {
                it.status = SessionItem.Companion.STATUS.UNDONE }

            _nextExerciseId.value = _actualExerciseId.value
            _actualExerciseId.value = _previousExerciseId.value
            _previousExerciseId.value = prevExeId
            Log.d(TAG,"prevExercise() - Status ${_state.value}, PreviousExeId = ${_previousExerciseId.value}, ActualExeId =" +
                    " ${_actualExerciseId.value}, NextExeId = ${_nextExerciseId.value} ")

            this.stopCountDown()
        }
    }

    fun unsetSessionItemToEdit() {
        _sessionItemToEdit.value = null
    }
    fun pauseCountDown() {
        if (state.value == TimerState.RUNNING) {
            countDownTimer.cancel()
            _state.value = TimerState.PAUSE
        }
        Log.d(TAG,"pauseCountDown() - Status = ${_state.value}")
    }

    fun getSessionItemList(): List<SessionItem>? {
        return sessionWorkout.value?.filter { it.exerciseId == actualExerciseId.value }
    }

    fun getActualSize(): Int? {
        return sessionWorkout.value?.filter { _actualExerciseId.value == it.exerciseId && it.status !=
                SessionItem.Companion.STATUS.PROTO }?.size
    }

    fun addSessionItemToSession() {
        val item = sessionWorkout.value?.lastOrNull { sessionItem ->
            sessionItem.exerciseId == _actualExerciseId.value &&
                    sessionItem.status != SessionItem.Companion.STATUS.PROTO  }
        var indexToShift = 0;
        viewModelScope.launch {
            sessionWorkout.value?.forEachIndexed { index, sessionItem ->
                if(sessionItem.exerciseId == _actualExerciseId.value && sessionItem.status ==
                    SessionItem.Companion.STATUS.PROTO) {
                    indexToShift = index;
                }
            }
        }
        item?.let {
            sessionWorkout.value?.add(indexToShift,sessionItemGenerator(item))
        }
        getFirstSessionItemRunningAvailable()?.status = SessionItem.Companion.STATUS.UNDONE
        countDownTimer.cancel()

        _state.value = TimerState.STOP

        Log.d(TAG,"addSessionItemToSession() - Status = ${_state.value}, iindexToShift =, $indexToShift")
    }

    fun setSessionItemToEdit(sessionItem: SessionItem, position: Int) {
        if(_state.value != TimerState.END) {
            this.pauseCountDown()
            _sessionItemToEdit.value = sessionItem
            _sessionItemToEditPosition = position
            Log.d(TAG, "setSessionItemToEdit() - sessionItemEditExerciseId =${_sessionItemToEdit.value?.exerciseId}," +
                    "sessionItemEditPosition = $_sessionItemToEditPosition")
        }
    }

    fun saveSessionItemToEdit(repetition: Int, duration: Int, weight: Int) {
        sessionWorkout.value?.filterIndexed { _, sessionItem ->
            sessionItem.exerciseId == _sessionItemToEdit.value?.exerciseId
        }?.forEachIndexed { index, sessionItem ->
            if(index == _sessionItemToEditPosition) {
                if(duration != 1) {
                    sessionItem.duration = TimeUnit.MINUTES.toSeconds(duration.toLong())
                }
                if(repetition != 1) {
                    sessionItem.repetition = repetition
                }
                if(weight != 1) {
                    sessionItem.weight = weight
                }
            }
        }
    }

    private fun stopCountDown() {
        if (state.value != TimerState.END) {
            countDownTimer.cancel()
            _state.value = TimerState.STOP
        }
        Log.d(TAG,"stopCountDown() - Status = ${_state.value}")
    }

    private fun transformToSessionWorkout(exe: List<ExerciseDetailed>?): MutableLiveData<LinkedList<SessionItem>> {
        val list: LinkedList<SessionItem> = LinkedList()
        val poll: MutableList<Long> = mutableListOf()
        var exeId = exe?.first()?.exerciseId
        exeId?.let {
            poll.add(it)
            exe?.forEachIndexed { index, exerciseDetailed ->
                if (exeId != exerciseDetailed.exerciseId) {

                    exeId?.let { it1 ->
                        poll.add(it1)
                        SessionItem(
                            index, exerciseDetailed.workoutId, it1, null, null, null,
                            SessionItem.Companion.STATUS.PROTO
                        )
                    }?.let { it2 -> list.add(it2) }
                    exeId = exerciseDetailed.exerciseId
                }
                exerciseDetailed.set?.let { it1 ->
                    repeat(it1, action = {
                        list.add(sessionItemGenerator(index,exerciseDetailed))
                    })
                }
            }
        }
        list.addLast(protoItemGenerator(list.last))
        exercisesPool = poll
        Log.d(TAG,"transformToSessionWorkout() - ExerciseLinkedList = $list, ExercisePoll = $exercisesPool}")
        return MutableLiveData(list)
    }

    private fun startCountDown() {
        Log.d(TAG,"startCountDown() - Status = ${_state.value},")

        if (state.value == TimerState.STOP || state.value == TimerState.END) {
            _state.value = TimerState.RUNNING
            val sessionItem = getFirstSessionItemAvailable()
            //If exist more set available
            if (sessionItem != null) {
                refreshTime(sessionItem)
                Log.d(TAG,"startCountDown() - Status = ${_state.value},NEW timeLeft =, ${_timeLeft.value},NEW timeTarget = $timeTarget")
                sessionItem.status = SessionItem.Companion.STATUS.DOING
                this.beginCountDown()
            } else {
                this.nextExercise()
                this.startCountDown()
            }
        } else if (state.value == TimerState.PAUSE) {
            _state.value = TimerState.RUNNING
            this.beginCountDown()
        }
    }

    private fun beginCountDown() {
        timeLeft.value?.let { time ->
            countDownTimer = object : CountDownTimer(time.toInt().toLong(), INTERVAL) {
                //every tick ill give the value to timeLeft
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft.value?.let { time ->
                        _timeLeft.value = millisUntilFinished
                        if ((millisUntilFinished * 0.001f).roundToInt() != (time * 0.001f).roundToInt()) {
                            Log.d(TAG, "beginCountDown() - TimeLeft = ${(_timeLeft.value!! * 0.001f).roundToInt()}")
                        }
                    }
                }

                override fun onFinish() {
                    _state.value = TimerState.STOP
                    //Status of SessionItem change to DONE
                   getFirstSessionItemRunningAvailable()?.status = SessionItem.Companion.STATUS.DONE
            if (getFirstSessionItemAvailable() == null) {
                //If doesn't exist any more set available at last exercise
                if (exercisesPool.indexOf(_actualExerciseId.value) == exercisesPool.lastIndex) {
                    endCountDown()
                } else {
                    Log.d(TAG, "last exe =${sessionWorkout.value?.
                    last()?.exerciseId}, actual exe = ${actualExerciseId.value}")
                    nextExercise()
                    startCountDown()
                }
            } else {
                startCountDown()
            }
        }
    }.start()
        }
    }

    fun endCountDown() {
        countDownTimer.cancel()
        _state.value = TimerState.END
        storeSession()
        _navigateToSummaryPage.value = true
    }

    private fun storeSession() {

        var completedRepetition = 0
        var completedSets = 0
        var totalRepetition = 0
        var totalSets = 0
        var rating = 0
        val workoutId = sessionWorkout.value?.first?.workoutId
        val duration = System.currentTimeMillis() - startTime

        sessionWorkout.value?.forEach { sessionItem ->
            if(sessionItem.status != SessionItem.Companion.STATUS.PROTO) {
                sessionItem.repetition?.let { repetition ->
                    totalRepetition += repetition
                }
                totalSets += 1
            }
            if(sessionItem.status == SessionItem.Companion.STATUS.DONE) {
                sessionItem.repetition?.let { repetition ->
                    completedRepetition += repetition
                }
                completedSets += 1
            }
        }


        var completed = 0.0
        if(completedRepetition == 0) {
            rating = 1
        } else {
            completed = (completedRepetition.toDouble()/totalRepetition.toDouble())*100
            rating = when {
                completed >= 80 -> {
                    5
                }
                completed >= 60 -> {
                    4
                }
                completed >= 40 -> {
                    3
                }
                completed >= 20 -> {
                    1
                }
                else -> {
                    1
                }
            }
            Log.d(TAG,"storeSession() - ")
        }


        Log.d(TAG,"storeSession() - totalRepetition = $totalRepetition," +
                " compeletedRepetition = $completedRepetition, completed $completed ")


        viewModelScope.launch {
                workoutId?.let {
                    sessionRepository.insert(Session(
                        workoutId = workoutId,
                        duration = duration,
                        rating = rating,
                        totalRepetitions = completedRepetition,
                        totalSets = completedSets,
                        completed = completed
                    )) }
        }

    }

    private fun getFirstSessionItemAvailable(): SessionItem? {
        return sessionWorkout.value?.firstOrNull { it.exerciseId == _actualExerciseId.value &&
                it.status == SessionItem.Companion.STATUS.UNDONE  }
    }

    private fun getFirstSessionItemRunningAvailable(): SessionItem? {
        return sessionWorkout.value?.firstOrNull { it.exerciseId == _actualExerciseId.value &&
                it.status == SessionItem.Companion.STATUS.DOING
        }
    }

    private fun getAllSessionItemRunningAvailable(): List<SessionItem>? {
        return sessionWorkout.value?.filter { it.exerciseId == _actualExerciseId.value &&
                it.status == SessionItem.Companion.STATUS.DOING
        }
    }

    private fun sessionItemGenerator(sItem: SessionItem): SessionItem {
        return SessionItem(
            id = sItem.id+100,
            exerciseId = sItem.exerciseId,
            workoutId = sItem.workoutId,
            weight = sItem.weight,
            duration = sItem.duration,
            repetition = sItem.repetition,
            status = SessionItem.Companion.STATUS.UNDONE)
    }

    private fun sessionItemGenerator(index: Int,exeDetailed: ExerciseDetailed): SessionItem {
        return SessionItem(
            id = index,
            exerciseId = exeDetailed.exerciseId,
            workoutId = exeDetailed.workoutId,
            weight = null,
            duration = exeDetailed.duration,
            repetition = exeDetailed.repetition,
            status = SessionItem.Companion.STATUS.UNDONE)
    }

    private fun protoItemGenerator(sessionItem: SessionItem): SessionItem {
        return  SessionItem(
            999, sessionItem.workoutId, sessionItem.exerciseId, null, null, null,
            SessionItem.Companion.STATUS.PROTO)
    }

    private fun refreshTime(sessionItem: SessionItem) {
        sessionItem.repetition?.let {  rep ->
            _timeLeft.value = 1000L * rep
            timeTarget = 1000L * rep
        }
        sessionItem.duration?.let { duration ->
            _timeLeft.value = duration
            timeTarget = duration
        }
    }

    companion object {
            enum class TimerState {
                RUNNING,PAUSE,STOP,END
            }
            const val INTERVAL = 10L
            const val TAG = "SessionViewModel"
        }
    }


