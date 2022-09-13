package com.pri.movies

import com.pri.movies.data.NetworkModule
import org.junit.Test
import retrofit2.Retrofit

class RetrofitClientTest {
    @Test
    fun testRetrofitInstance() {
        //Get an instance of Retrofit
        val instance: Retrofit = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
        //Assert that, Retrofit's base url matches to our BASE_URL
        assert(instance.baseUrl().toString() == BuildConfig.BASE_URL)
    }
}
