package com.smitch.nsddemo.ui.main

import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smitch.nsddemo.R
import com.smitch.nsddemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ClickListener {
    lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModel.Factory
    private val context: MainActivity by lazy { this }

    private var serviceList = arrayListOf<ServiceModel>()

    private lateinit var mainActivityBinding: ActivityMainBinding

    lateinit var adapter: AvailableServiceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.setContentView(context, R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        mainActivityBinding.scan = context
        setupViewModel()
        setUpServiceListAdapter()
    }

    private fun setupViewModel() {
        viewModelFactory = MainViewModel.Factory(context)
        mainViewModel = ViewModelProvider(context, viewModelFactory)
            .get(MainViewModel::class.java)



        mainViewModel.serviceListData.observe(this, Observer {

            it?.let {
                serviceList.addAll(it as List<ServiceModel>)
                adapter.notifyDataSetChanged()
                mainViewModel.stopDiscovery()
            }
        })

    }

    private fun setUpServiceListAdapter() {
        adapter = AvailableServiceAdapter(serviceList)
        mainActivityBinding.rvAvailableServiceList.adapter = adapter
    }

    override fun onClicked(view: View) {
        when (view.id) {
            R.id.btnScan -> {
                mainViewModel.discoverService()
            }
            R.id.btnPublish -> {
                mainViewModel.registerService()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.unregister()
    }


}