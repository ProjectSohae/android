package com.sohae.common.di.repositorymodule

import com.sohae.common.di.client.RetrofitClient
import com.sohae.data.community.repositoryimpl.PostRepositoryImpl
import com.sohae.data.jobinformation.repositoryimpl.JobInfoRepositoryImpl
import com.sohae.data.myinformation.repositoryimpl.MyInfoRepositoryImpl
import com.sohae.data.profile.repositoryimpl.ProfileRepositoryImpl
import com.sohae.data.session.repositoryimpl.SessionRepositoryImpl
import com.sohae.domain.community.repository.PostRepository
import com.sohae.domain.jobinformation.repository.JobInfoRepository
import com.sohae.domain.myinformation.repository.MyInfoRepository
import com.sohae.domain.profile.repository.ProfileReposiotory
import com.sohae.domain.session.repository.SessionRepository
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
    fun providePostRepository(): PostRepository {
        return PostRepositoryImpl(RetrofitClient.retrofitWithToken)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(): SessionRepository {
        return SessionRepositoryImpl(
            RetrofitClient.retrofit,
            RetrofitClient.retrofitWithToken
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileReposiotory {
        return ProfileRepositoryImpl(RetrofitClient.retrofit)
    }

    @Provides
    @Singleton
    fun provideJobInfoRepository(): JobInfoRepository {
        return JobInfoRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideMyInfoRepository(): MyInfoRepository {
        return MyInfoRepositoryImpl
    }
}