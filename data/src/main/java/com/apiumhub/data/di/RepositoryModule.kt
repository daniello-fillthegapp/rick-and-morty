package com.apiumhub.data.di

import com.apiumhub.data.datasource.LocalDataSource
import com.apiumhub.data.datasource.NetworkDataSource
import com.apiumhub.data.datasource.RemoteDataSource
import com.apiumhub.data.repository.CharacterRepositoryImpl
import com.apiumhub.data.repository.NetworkRepositoryImpl
import com.apiumhub.domain.repository.CharacterRepository
import com.apiumhub.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): CharacterRepository {
        return CharacterRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        networkDataSource: NetworkDataSource
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            networkDataSource = networkDataSource
        )
    }
}