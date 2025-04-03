package com.apiumhub.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun isNetworkAvailable(): Flow<Boolean>
}