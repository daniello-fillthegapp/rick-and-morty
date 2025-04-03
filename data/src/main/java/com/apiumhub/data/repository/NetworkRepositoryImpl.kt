package com.apiumhub.data.repository

import com.apiumhub.data.datasource.NetworkDataSource
import com.apiumhub.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) : NetworkRepository {

    override fun isNetworkAvailable(): Flow<Boolean> {
        return networkDataSource.isNetworkAvailable()
    }
}