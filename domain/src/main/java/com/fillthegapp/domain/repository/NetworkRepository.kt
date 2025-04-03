package com.fillthegapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun isNetworkAvailable(): Flow<Boolean>
}