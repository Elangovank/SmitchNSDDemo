package com.smitch.nsddemo.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel(protected val mContext: Context): ViewModel() {



     fun print(){
        Log.e("Success", "Success")
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(context) as T
        }
    }

}