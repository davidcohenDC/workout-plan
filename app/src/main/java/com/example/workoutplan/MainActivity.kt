package com.example.workoutplan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toasty.Config.getInstance().allowQueue(false).apply()
    }


}
