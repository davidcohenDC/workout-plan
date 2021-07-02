package com.example.workoutplan

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.workoutplan.databinding.ActivityMainBinding
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toasty.Config.getInstance().allowQueue(false).apply()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.let {
            setSupportActionBar(binding.toolbar)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = "ciao"
        }

        val toggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_open, R.string.navigation_close) {
        }
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener { item ->
            Toasty.normal(applicationContext, item.title.toString() + " Selected", Toasty.LENGTH_SHORT).show()
            actionBar?.title = item.title
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp() || super.onNavigateUp()
    }
}