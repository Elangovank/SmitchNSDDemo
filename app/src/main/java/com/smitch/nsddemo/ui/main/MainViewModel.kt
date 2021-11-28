package com.smitch.nsddemo.ui.main

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.smitch.nsddemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.util.*

class MainViewModel(protected val context: Context) : ViewModel(),
    NsdManager.RegistrationListener {

    private val SERVICE_NAME = "myLocalHost"
    private val SERVICE_TYPE = "_http._tcp."
    private val SERVICE_PORT = 80
    private var mNsdManager: NsdManager? = null

    private var isDiscoveredStarted = false
    private var isPublished = false

    var serviceListData = MutableLiveData<List<ServiceModel?>>()

    val list: ArrayList<ServiceModel> = arrayListOf()
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
        if (!isPublished) {
            viewModelScope.launch(Dispatchers.IO) {
                mNsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

                val inet = InetAddress.getByName("10.0.0.2")
                val serviceInfo = NsdServiceInfo().apply {
                    serviceName = SERVICE_NAME
                    serviceType = SERVICE_TYPE
                    host = inet
                    port = SERVICE_PORT
                }

                mNsdManager?.registerService(
                    serviceInfo,
                    NsdManager.PROTOCOL_DNS_SD,
                    this@MainViewModel
                )
                isPublished = true
            }

        } else {
            display(context.getString(R.string.msg_published_already))
        }
    }

    fun stopDiscovery() {
        if (isDiscoveredStarted) {
            mNsdManager?.apply {
                stopServiceDiscovery(discoverListener)
            }
        }
        isDiscoveredStarted = false
    }

    fun discoverService() {
        CoroutineScope(Dispatchers.IO).launch {
            list.clear()
            mNsdManager!!.discoverServices(
                SERVICE_TYPE,
                NsdManager.PROTOCOL_DNS_SD, discoverListener
            )
        }
        isDiscoveredStarted = true
    }

    fun unregister() {
        print(Thread.currentThread().name)
        viewModelScope.launch(Dispatchers.IO) {
            print(Thread.currentThread().name)
            mNsdManager?.apply {
                unregisterService(this@MainViewModel)
                stopServiceDiscovery(discoverListener)
            }
        }


    }

    val discoverListener = object : NsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
        }

        override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
        }

        override fun onDiscoveryStarted(p0: String?) {
        }

        override fun onDiscoveryStopped(p0: String?) {
        }

        override fun onServiceFound(p0: NsdServiceInfo?) {

            Log.e("onServiceFound ", p0?.serviceName ?: "")
            Log.e("onServiceFound ", p0?.serviceType ?: "")
            Log.e("onServiceFound ", p0?.host.toString() ?: "")
            Log.e("onServiceFound ", p0?.port.toString() ?: "")


            viewModelScope.launch(Dispatchers.IO) {
                mNsdManager!!.resolveService(p0, object : NsdManager.ResolveListener {
                    override fun onResolveFailed(p0: NsdServiceInfo?, p1: Int) {
                    }

                    override fun onServiceResolved(p0: NsdServiceInfo?) {
                        if (SERVICE_TYPE.equals(p0?.serviceType)) {
                            list.add(
                                ServiceModel(
                                    p0?.serviceName ?: "",
                                    p0?.serviceType ?: "",
                                    p0?.host.toString() ?: "",
                                    p0?.port.toString() ?: ""

                                )
                            )
                            serviceListData.postValue(list)
                        }
                    }

                })

            }
        }

        override fun onServiceLost(p0: NsdServiceInfo?) {
        }

    }


    override fun onRegistrationFailed(p0: NsdServiceInfo?, p1: Int) {
        display(context.getString(R.string.msg_publish_failure))
    }

    override fun onUnregistrationFailed(p0: NsdServiceInfo?, p1: Int) {

    }

    override fun onServiceRegistered(p0: NsdServiceInfo?) {
        display(context.getString(R.string.msg_publish_success))
    }

    override fun onServiceUnregistered(p0: NsdServiceInfo?) {

    }

    fun display(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

