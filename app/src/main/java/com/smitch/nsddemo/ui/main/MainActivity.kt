package com.smitch.nsddemo.ui.main

import android.os.Bundle
import android.os.StrictMode
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
        mainActivityBinding = DataBindingUtil.setContentView(context, R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mainActivityBinding.scan = context
        setupViewModel()
        mainViewModel.print("Success")
        //  mainViewModel.registerService()
    }

    private fun setupViewModel() {
        viewModelFactory = MainViewModel.Factory(context)
        mainViewModel = ViewModelProvider(context, viewModelFactory)
            .get(MainViewModel::class.java)

    }

    override fun onScanClicked(view: View) {
        when (view.id) {
            R.id.btnScan -> {
                mainViewModel.discoverService()
            }
            R.id.btnPublish -> {
                mainViewModel.registerService()
            }
        }

    }

    fun printToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.unregister()
    }
}