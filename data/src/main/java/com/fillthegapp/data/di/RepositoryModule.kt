package com.fillthegapp.data.di

import com.fillthegapp.data.datasource.LocalDataSource
import com.fillthegapp.data.datasource.NetworkDataSource
import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.data.repository.CharacterRepositoryImpl
import com.fillthegapp.data.repository.NetworkRepositoryImpl
import com.fillthegapp.domain.repository.CharacterRepository
import com.fillthegapp.domain.repository.NetworkRepository
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