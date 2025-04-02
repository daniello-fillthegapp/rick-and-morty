package com.fillthegapp.domain.usecase

import com.fillthegapp.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNetworkAvailabilityUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    fun execute(): Flow<Boolean> {
        return networkRepository.isNetworkAvailable()
    }
}