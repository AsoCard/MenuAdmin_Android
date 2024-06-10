package com.aso.asomenuadmin.di

import android.content.Context
import com.aso.asomenuadmin.network.ApiService
import com.aso.asomenuadmin.network.util.NetworkUtil
import com.aso.asomenuadmin.repository.ImageRepository
import com.aso.asomenuadmin.repository.Repository
import com.aso.asomenuadmin.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    //image repository
    @Singleton
    @Provides
    fun provideImageRepository(@ApplicationContext context: Context,api: ApiService ,networkUtil: NetworkUtil): ImageRepository {
        return ImageRepository(context , networkUtil,api)
    }
    @Provides
    @Singleton
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

    @Singleton
    @Provides
    fun provideRepository(api: ApiService ,networkUtil: NetworkUtil): Repository {
        return RepositoryImpl(api,networkUtil)
    }

    @Singleton
    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService {
        // Provide your API service interface implementation
        return retrofitBuilder.build().create(ApiService::class.java)
    }

}