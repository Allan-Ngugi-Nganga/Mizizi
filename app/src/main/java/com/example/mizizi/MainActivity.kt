package com.example.mizizi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mizizi.databinding.ActivityMainBinding

/**
 * The single Activity that hosts the app. Screens are Fragments inside its
 * NavHost, so everything after the splash lives in `res/navigation/nav_graph.xml`.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.placeholderTextView.text = getString(R.string.scaffold_message)
    }
}
