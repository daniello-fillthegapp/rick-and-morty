package com.fillthegapp.data.di

import android.content.Context
import androidx.room.Room
import com.fillthegapp.data.datasource.NetworkDataSource
import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.data.persistance.CharacterDao
import com.fillthegapp.data.persistance.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            "character_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: CharacterDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideRemoteDatasource(
        retrofit: Retrofit
    ): RemoteDataSource {
        return retrofit.create(RemoteDataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(@ApplicationContext context: Context): NetworkDataSource {
        return NetworkDataSource(context)
    }
}