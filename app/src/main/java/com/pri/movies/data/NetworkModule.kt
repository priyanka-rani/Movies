package com.pri.movies.data

import android.content.Context
import com.google.gson.GsonBuilder
import com.pri.movies.BuildConfig
import com.pri.movies.utils.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val cacheSize = 10L * 1024 * 1024 // 10 MB

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cache(Cache(context.cacheDir, cacheSize))
            .addInterceptor { chain ->
                val original = chain.request()

                /* add api key and type to every request */
                val url = original.url.newBuilder()
                    .addQueryParameter("apikey", BuildConfig.API_KEY)
                    .addQueryParameter("type", "movie")
                    .build()

                val request = original.newBuilder()
                    .url(url)
                    .header(
                        "Cache-Control",
                        if (NetworkUtil.hasNetwork(context)) "public, max-age=" + 5
                        else "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()

                chain.proceed(request)
            }.apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()


    @Provides
    @Singleton
    internal fun provideApiService(
        retrofit: Retrofit
    ) = retrofit.create(ApiService::class.java)

}