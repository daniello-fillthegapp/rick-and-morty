package com.fillthegapp.data.di

import com.fillthegapp.data.datasource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDatasource(
        retrofit: Retrofit
    ): RemoteDataSource {
        return retrofit.create(RemoteDataSource::class.java)
    }
}