package com.smitch.nsddemo.ui.main

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class MainViewModel(protected val mContext: Context) : ViewModel(),
    NsdManager.RegistrationListener {

    private val SERVICE_NAME = "myLocalHost"
    private val SERVICE_TYPE = "_http._tcp."
    private val SERVICE_PORT = 80
    private var mNsdManager: NsdManager? = null


    fun print(msg: String) {
        Log.e(msg, msg)
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(context) as T
        }
    }


    fun registerService() {

        print(Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            print(Thread.currentThread().name)
            mNsdManager = mContext.getSystemService(Context.NSD_SERVICE) as NsdManager

            val inet = InetAddress.getByName("10.0.0.2")
            Log.e("address", inet.hostAddress)
            val serviceInfo = NsdServiceInfo()
            serviceInfo.serviceName = SERVICE_NAME
            serviceInfo.serviceType = SERVICE_TYPE
            serviceInfo.host = inet
            serviceInfo.port = SERVICE_PORT

            mNsdManager!!.registerService(
                serviceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                this@MainViewModel
            )

        }


    }



    fun unregister() {
        print(Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            print(Thread.currentThread().name)
            mNsdManager?.apply {
                unregisterService(this@MainViewModel)

            }
        }


    }




    override fun onRegistrationFailed(p0: NsdServiceInfo?, p1: Int) {

    }

    override fun onUnregistrationFailed(p0: NsdServiceInfo?, p1: Int) {

    }

    override fun onServiceRegistered(p0: NsdServiceInfo?) {
        Log.d(
            "NsdserviceOnUnregister",
            "Service Unregistered : " + p0?.serviceName
        )

    }

    override fun onServiceUnregistered(p0: NsdServiceInfo?) {

    }
}

