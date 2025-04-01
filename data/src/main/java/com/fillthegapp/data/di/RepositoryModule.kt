package com.fillthegapp.data.di

import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.data.repository.CharacterRepositoryImpl
import com.fillthegapp.domain.repository.CharacterRepository
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
        remoteDataSource: RemoteDataSource
    ): CharacterRepository {
        return CharacterRepositoryImpl(
            remoteDataSource = remoteDataSource
        )
    }
}