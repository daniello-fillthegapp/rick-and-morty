package com.apiumhub.domain.usecase

import com.apiumhub.domain.repository.ConnectivityRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveNetworkAvailabilityUseCase @Inject constructor(
    private val connectivityRepository: ConnectivityRepository
) {
    fun execute(): StateFlow<Boolean> {
        return connectivityRepository.isNetworkAvailable()
    }
}