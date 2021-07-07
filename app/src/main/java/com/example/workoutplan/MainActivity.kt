package com.example.workoutplan

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.example.workoutplan.databinding.ActivityMainBinding
import com.example.workoutplan.fragments.AboutFragment
import com.example.workoutplan.fragments.HomeFragment
import com.google.android.material.appbar.MaterialToolbar
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var activityToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toasty.Config.getInstance().allowQueue(false).apply()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = "ciao"
        }
    }

    fun toogleMenu(toolbar: Toolbar?) {
        activityToolbar = toolbar
        val toggle = object : ActionBarDrawerToggle(this, binding.drawerLayout,activityToolbar, R.string.navigation_open, R.string.navigation_close) {
        }
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if(supportFragmentManager.findFragmentById(R.id.nav_hostt)?.childFragmentManager?.fragments?.get(0) is HomeFragment
                || supportFragmentManager.findFragmentById(R.id.nav_hostt) is HomeFragment
                || supportFragmentManager.findFragmentById(R.id.homeFragment2)?.isVisible == true) {
            binding.navView.setCheckedItem(R.id.selection_home)
        } else if(supportFragmentManager.findFragmentById(R.id.nav_hostt)?.childFragmentManager?.fragments?.get(0) is AboutFragment
                || supportFragmentManager.findFragmentById(R.id.nav_hostt) is AboutFragment
                || supportFragmentManager.findFragmentById(R.id.aboutFragment)?.isVisible == true) {
            binding.navView.setCheckedItem(R.id.selection_about)
        } else {
            binding.navView.checkedItem?.isChecked = false
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.selection_home -> {
                        if(supportFragmentManager.findFragmentById(R.id.nav_hostt)?.childFragmentManager?.fragments?.get(0) !is HomeFragment) {
                            findNavController(R.id.nav_hostt).navigate(R.id.homeFragment2)
                        }
                    binding.drawerLayout.closeDrawers()
                    true

                }
                R.id.selection_about -> {
                    if(supportFragmentManager.findFragmentById(R.id.nav_hostt)?.childFragmentManager?.fragments?.get(0) !is AboutFragment) {
                        findNavController(R.id.nav_hostt).navigate(R.id.aboutFragment)
                    }
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> true
            }

        }
    }

    
    override fun onNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_hostt)
        return navController.navigateUp() || super.onNavigateUp()
    }

}