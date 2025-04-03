package com.apiumhub.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityRepository {
    fun isNetworkAvailable(): StateFlow<Boolean>
}