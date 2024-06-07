package com.aso.asomenuadmin.di

import android.content.Context
import com.aso.asomenuadmin.network.ApiService
import com.aso.asomenuadmin.repository.ImageRepository
import com.aso.asomenuadmin.repository.RecipeRepository
import com.aso.asomenuadmin.repository.RecipeRepositoryImpl
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
    fun provideImageRepository(@ApplicationContext context: Context): ImageRepository {
        return ImageRepository(context)
    }

    @Singleton
    @Provides
    fun provideRecipeRepository(api: ApiService): RecipeRepository {
        return RecipeRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideAuthApiService(retrofitBuilder: Retrofit.Builder): ApiService {
        // Provide your API service interface implementation
        return retrofitBuilder.build().create(ApiService::class.java)
    }

}