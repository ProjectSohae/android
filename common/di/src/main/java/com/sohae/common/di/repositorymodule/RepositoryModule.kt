package com.sohae.common.di.repositorymodule

import com.sohae.common.di.remote.RetrofitClient
import com.sohae.data.community.repositoryimpl.PostRepositoryImpl
import com.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.sohae.data.signin.repositoryimpl.SignInRepositoryImpl
import com.sohae.domain.community.repository.PostRepository
import com.sohae.domain.myinformation.repository.MyInfoRepository
import com.sohae.domain.signin.repository.SignInRepository
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
    fun providePostRepository(): com.sohae.domain.community.repository.PostRepository {
        return PostRepositoryImpl(RetrofitClient.retrofit)
    }

    @Provides
    @Singleton
    fun providerSignInRepository(): com.sohae.domain.signin.repository.SignInRepository {
        return SignInRepositoryImpl(RetrofitClient.retrofit)
    }

    @Provides
    @Singleton
    fun provideMyInfoRepository(): com.sohae.domain.myinformation.repository.MyInfoRepository {
        return MyInfoRepositoryImpl
    }
}