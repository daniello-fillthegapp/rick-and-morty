package com.fillthegapp.data.datasource

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    context: Context
) {
    private val mConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isNetworkAvailable = MutableStateFlow(false)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isNetworkAvailable.update { true }
        }

        override fun onLost(network: Network) {
            _isNetworkAvailable.update { false }
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder().build()
        mConnectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun isNetworkAvailable(): StateFlow<Boolean> {
        return _isNetworkAvailable.asStateFlow()
    }
}