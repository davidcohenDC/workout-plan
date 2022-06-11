package com.example.workoutplan.viewmodels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.data.relations.SessionItem
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class SessionViewModel(
    workoutExeRepository: WorkoutExerciseCrossRefRepository,
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


    private val _sessionItemToEdit: MutableLiveData<SessionItem?> = MutableLiveData(null)
    val sessionItemToEdit: LiveData<SessionItem?>
        get() = _sessionItemToEdit

    var _sessionItemToEditPosition: Int? = null

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
    private val _state: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOP)
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

        fun onNavigateToWorkoutExercisePage(workout: Workout) {
            _navigateToWorkoutExercisePage.value = workout.workoutId
        }

        fun navigateToWorkoutExercisePageDone() {
            _navigateToWorkoutExercisePage.value = null
        }

        fun toggleButton() {
            if (state.value == TimerState.STOP || state.value == TimerState.PAUSE) {
                startTimer()
            } else {
                this.pause()
            }
        }

        fun getActualTime(): Int {
            return (timeLeft.value?.times(0.001f))?.roundToInt() ?: 0
        }

        private fun startTimer() {
            if (state.value == TimerState.STOP) {
                val sessionItem = getSessionItemAvailable()
                //If exist more set available
                if (sessionItem != null) {
                    if (sessionItem.duration == null) {
                        _timeLeft.value = 1000L * sessionItem.repetition!!
                        timeTarget = 1000L * sessionItem.repetition!!
                    } else {
                        _timeLeft.value = sessionItem.duration!!
                        timeTarget = sessionItem.duration!!
                    }

                    _state.value = TimerState.RUNNING
                    sessionWorkout.value?.firstOrNull {
                        it.exerciseId == actualExerciseId.value && it.status == SessionItem.Companion.STATUS.UNDONE
                    }?.status = SessionItem.Companion.STATUS.DOING
                    beginCountDown()
                }
            } else if (state.value == TimerState.PAUSE) {
                _state.value = TimerState.RUNNING
                beginCountDown()
            }
        }


        private fun pause() {
            if (state.value == TimerState.RUNNING) {
                countDownTimer.cancel()
                _state.value = TimerState.PAUSE
            }
        }

        fun nextExercise() {
            countDownTimer.cancel()
            _state.value = TimerState.STOP
            changeActualExerciseToNext()
            startTimer()
        }

        fun prevExercise() {

        }

        private fun beginCountDown() {
            timeLeft.value?.let { time ->
                countDownTimer = object : CountDownTimer(time.toInt().toLong(), INTERVAL) {

                    //every tick ill give the value to timeLeft
                    override fun onTick(millisUntilFinished: Long) {
                        timeLeft.value?.let { time ->
                            _timeLeft.value = millisUntilFinished
                            if ((millisUntilFinished * 0.001f).roundToInt() != (time * 0.001f).roundToInt()) {
                                Log.i("COUNTDOWN", "${(_timeLeft.value!! * 0.001f).roundToInt()}")
                            }
                        }
                    }

                    override fun onFinish() {
                        _state.value = TimerState.STOP
                        //Status of SessionItem change to DONE
                        sessionWorkout.value?.firstOrNull {
                            it.exerciseId == actualExerciseId.value && it.status == SessionItem.Companion.STATUS.DOING
                        }?.status = SessionItem.Companion.STATUS.DONE

                        if (getSessionItemAvailable() == null) {
                            //If doesn't exist any more set available at last exercise
                            if (sessionWorkout.value?.last()?.exerciseId == actualExerciseId.value) {
                                endCountDown()
                            } else {
                                changeActualExerciseToNext()
                                startTimer()
                            }
                        } else {
                            startTimer()
                        }
                    }
                }.start()
            }
        }

        private fun endCountDown() {
            _state.value = TimerState.END
            _navigateToSummaryPage.value = true
        }


        private fun transformToSessionWorkout(exe: List<ExerciseDetailed>?): MutableLiveData<LinkedList<SessionItem>> {
            val list: LinkedList<SessionItem> = LinkedList()
            var exeId = exe?.first()?.exerciseId
            exe?.forEachIndexed { index, exerciseDetailed ->

                if (exeId != exerciseDetailed.exerciseId) {
                    exeId?.let { it1 ->
                        SessionItem(
                            index, exerciseDetailed.workoutId, it1, null, null, null,
                            SessionItem.Companion.STATUS.PROTO
                        )
                    }?.let { it2 -> list.add(it2) }
                    exeId = exerciseDetailed.exerciseId
                }
                exerciseDetailed.set?.let { it1 ->
                    repeat(it1, action = {
                        list.add(
                            SessionItem(
                                index,
                                exerciseDetailed.workoutId,
                                exerciseDetailed.exerciseId,
                                exerciseDetailed.repetition,
                                exerciseDetailed.duration,
                                null,
                                SessionItem.Companion.STATUS.UNDONE
                            )
                        )
                    })
                }

            }
            list.addLast(
                SessionItem(
                    999, list.last.workoutId, list.last.exerciseId, null, null, null,
                    SessionItem.Companion.STATUS.PROTO
                )
            )
            return MutableLiveData(list)
        }

        private fun getNextExerciseId(): Long? {
            var index = sessionWorkout.value?.indexOf(getLastItemAvailable())
            return if (index != -1) {
                index = index?.plus(1)
                index?.let {
                    if (sessionWorkout.value?.get(it) != null) {
                        sessionWorkout.value?.get(it)?.exerciseId
                    } else {
                        null
                    }
                }
            } else {
                null
            }
        }

        private fun changeActualExerciseToNext() {
            _previousExerciseId.value = actualExerciseId.value
            _actualExerciseId.value = _nextExerciseId.value
            _nextExerciseId.value = getNextExerciseId()
        }

        private fun getSessionItemAvailable(): SessionItem? {
            return sessionWorkout.value?.firstOrNull {
                it.exerciseId == actualExerciseId.value && it.status == SessionItem.Companion.STATUS.UNDONE
            }
        }

        private fun getLastItemAvailable(): SessionItem? {
            return sessionWorkout.value?.firstOrNull {
                it.exerciseId == actualExerciseId.value && it.status == SessionItem.Companion.STATUS.PROTO
            }
        }

        fun setSessionItemToEdit(sessionItem: SessionItem, position: Int) {
            _sessionItemToEdit.value = sessionItem
            _sessionItemToEditPosition = position
        }
    fun setSessionItemToEdit(repetition: Int, duration: Int, weight: Int) {
        val newList: LinkedList<SessionItem> = LinkedList()
        sessionWorkout.value?.let { sw ->
            _sessionItemToEdit.value?.let { itemEdit ->
                sw.forEachIndexed { index, item ->
                    if(index == _sessionItemToEditPosition) {
                        if(duration != 1) {
                            item.duration = TimeUnit.MINUTES.toSeconds(duration.toLong())
                        }
                        if(repetition != 1) {
                            item.repetition = repetition
                        }
                        if(weight != 1) {
                            item.weight = weight
                        }
                    }
                    newList.add(item)
                }
            }
            //sessionWorkout.value = newList
        }
    }
        fun unsetSessionItemToEdit() {
            _sessionItemToEdit.value = null
        }
        fun getSessionItemEditDuration(): Int {
            return sessionItemToEdit.value?.duration?.let { TimeUnit.SECONDS.toMinutes(it).toInt() } ?: 1
        }


    fun getSessionItemList(): List<SessionItem>? {
            return sessionWorkout.value?.filter { it.exerciseId == actualExerciseId.value }
        }
        companion object {
            enum class TimerState {
                RUNNING,PAUSE,STOP,END
            }
            const val INTERVAL = 10L
            const val TAG = "SessionViewModel"
        }
    }


