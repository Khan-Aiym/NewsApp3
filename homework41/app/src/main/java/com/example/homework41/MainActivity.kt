package com.example.homework41

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.homework41.databinding.ActivityMainBinding

class MainActivity constructor() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        val appBarConfiguration: AppBarConfiguration = Builder(
            R.id.navigation_home, R.id.navigation_dashboard,
            R.id.navigation_notifications, R.id.profileFragment
        ).build()

        val navController: NavController =
            findNavController(this, R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(binding.navView, navController)
        if (prefs!!.isBoardShown) {
            navController.navigate(R.id.loginFragment)
        }
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            if (navDestination.id == R.id.boardFragment || navDestination.id == R.id.loginFragment) {
                binding.navView.visibility = View.GONE
                supportActionBar!!.hide()
            } else {
                binding.navView.visibility = View.VISIBLE
                supportActionBar!!.show()
            }
        }
    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clean) {
            prefs!!.cleanPreferences()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var prefs: Prefs? = null
    }
}