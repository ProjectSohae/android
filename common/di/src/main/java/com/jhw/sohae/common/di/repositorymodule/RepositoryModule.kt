package com.jhw.sohae.common.di.repositorymodule

import com.jhw.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.jhw.sohae.domain.myinformation.repository.MyInfoRepository
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