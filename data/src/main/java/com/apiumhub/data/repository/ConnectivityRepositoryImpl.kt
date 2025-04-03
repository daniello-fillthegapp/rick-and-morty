package com.apiumhub.data.repository

import com.apiumhub.data.datasource.ConnectivityDataSource
import com.apiumhub.domain.repository.ConnectivityRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ConnectivityRepositoryImpl @Inject constructor(
    private val connectivityDataSource: ConnectivityDataSource,
) : ConnectivityRepository {

    override fun isNetworkAvailable(): StateFlow<Boolean> {
        return connectivityDataSource.isNetworkAvailable()
    }
}