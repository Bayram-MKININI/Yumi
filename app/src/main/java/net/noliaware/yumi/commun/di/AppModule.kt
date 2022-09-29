package net.noliaware.yumi.commun.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.noliaware.yumi.commun.BASE_URL
import net.noliaware.yumi.commun.data.remote.RemoteApi
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.feature_alerts.data.repository.AlertsRepository
import net.noliaware.yumi.feature_alerts.data.repository.AlertsRepositoryImpl
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepository
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepositoryImpl
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepository
import net.noliaware.yumi.feature_login.data.repository.DataStoreRepositoryImpl
import net.noliaware.yumi.feature_login.data.repository.LoginRepository
import net.noliaware.yumi.feature_login.data.repository.LoginRepositoryImpl
import net.noliaware.yumi.feature_message.data.repository.MessageRepository
import net.noliaware.yumi.feature_message.data.repository.MessageRepositoryImpl
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepository
import net.noliaware.yumi.feature_profile.data.repository.ProfileRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSessionData() = SessionData()

/*
    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .followRedirects(true)
            .build()
    } else OkHttpClient
        .Builder()
        .followRedirects(true)
        .build()*/

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .followRedirects(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            )
        ).baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): RemoteApi = retrofit.create(RemoteApi::class.java)

    @Provides
    fun provideLoginRepository(api: RemoteApi, sessionData: SessionData): LoginRepository {
        return LoginRepositoryImpl(api, sessionData)
    }

    @Provides
    fun provideCategoryRepository(api: RemoteApi, sessionData: SessionData): CategoryRepository {
        return CategoryRepositoryImpl(api, sessionData)
    }

    @Provides
    fun provideProfileRepository(api: RemoteApi, sessionData: SessionData): ProfileRepository {
        return ProfileRepositoryImpl(api, sessionData)
    }

    @Provides
    fun provideMessageRepository(api: RemoteApi, sessionData: SessionData): MessageRepository {
        return MessageRepositoryImpl(api, sessionData)
    }

    @Provides
    fun provideAlertsRepository(api: RemoteApi, sessionData: SessionData): AlertsRepository {
        return AlertsRepositoryImpl(api, sessionData)
    }
}