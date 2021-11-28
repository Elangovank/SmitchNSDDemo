package com.smitch.nsddemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.smitch.nsddemo.R
import com.smitch.nsddemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ClickListener {
    lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModel.Factory
    private val context: MainActivity by lazy { this }

    private lateinit var mainActivityBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil. setContentView(context, R.layout.activity_main)
       mainActivityBinding.scan = context
        setupViewModel()
        mainViewModel.print()
    }

    private fun setupViewModel() {
        viewModelFactory = MainViewModel.Factory(context)
        mainViewModel = ViewModelProvider(context, viewModelFactory)
            .get(MainViewModel::class.java)

    }
    override fun onClicked(view : View){
         printToast("Scan clicked")
    }

    private fun printToast(msg: String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }
}