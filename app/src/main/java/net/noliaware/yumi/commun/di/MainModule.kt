package net.noliaware.yumi.commun.di

import androidx.lifecycle.SavedStateHandle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.commun.BASE_URL
import net.noliaware.yumi.data.repository.Repository
import net.noliaware.yumi.data.repository.RepositoryImpl
import net.noliaware.yumi.data.remote.RemoteApi
import net.noliaware.yumi.domain.model.SessionData
import net.noliaware.yumi.presentation.controllers.LoginFragmentViewModel
import net.noliaware.yumi.presentation.controllers.VouchersListFragmentViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { provideSessionData() }
    single { provideOkHttpClient() }
    single { provideRetrofit(okHttpClient = get()) }
    single { provideApiService(retrofit = get()) }
    single { provideMediaRepository(api = get(), sessionData = get()) }
}

private fun provideSessionData() = SessionData()

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {

    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .followRedirects(true)
        .build()
} else OkHttpClient
    .Builder()
    .followRedirects(true)
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient
): Retrofit = Retrofit.Builder()
    .addConverterFactory(
        MoshiConverterFactory.create(Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build())
    ).baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

private fun provideApiService(retrofit: Retrofit): RemoteApi =
    retrofit.create(RemoteApi::class.java)

private fun provideMediaRepository(api: RemoteApi, sessionData: SessionData): Repository {
    return RepositoryImpl(api, sessionData)
}

val loginFragmentVMModule = module {
    viewModel {
        LoginFragmentViewModel(
            repository = get()
        )
    }
}

val voucherListFragmentVMModule = module {
    viewModel { (handle: SavedStateHandle) ->
        VouchersListFragmentViewModel(
            savedStateHandle = handle,
            repository = get()
        )
    }
}