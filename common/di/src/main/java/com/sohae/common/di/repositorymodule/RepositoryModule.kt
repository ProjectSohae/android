package com.sohae.common.di.repositorymodule

import com.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.sohae.domain.myinformation.repository.MyInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMyInfoRepository(): MyInfoRepository {
        return MyInfoRepositoryImpl
    }
}