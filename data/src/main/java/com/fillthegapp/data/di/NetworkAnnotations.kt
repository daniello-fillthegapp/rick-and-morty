package com.fillthegapp.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JSONConverter