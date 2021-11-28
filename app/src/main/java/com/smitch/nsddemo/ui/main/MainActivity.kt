package com.smitch.nsddemo.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.smitch.nsddemo.R

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModel.Factory
    private val context: Context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        mainViewModel.print()
    }

    private fun setupViewModel() {
        viewModelFactory = MainViewModel.Factory(context)
        mainViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

    }
}