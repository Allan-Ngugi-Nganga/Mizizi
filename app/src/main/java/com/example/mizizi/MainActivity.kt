package com.example.mizizi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.mizizi.databinding.ActivityMainBinding

/**
 * The single Activity in the app.
 *
 * It owns the toolbar and the NavHost; every screen after it is a Fragment
 * listed in `res/navigation/nav_graph.xml`.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        // Top level screens do not get an Up arrow, everything else does.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
