package com.fillthegapp.data.repository

import com.fillthegapp.data.datasource.NetworkDataSource
import com.fillthegapp.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) : NetworkRepository {

    override fun isNetworkAvailable(): Flow<Boolean> {
        return networkDataSource.isNetworkAvailable()
    }
}