package com.gadgetsfury.electionindia.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private const val CONNECT_TIMEOUT = 30.toLong()
        private const val READ_TIMEOUT = 30.toLong()
        private const val WRITE_TIMEOUT = 30.toLong()

        private const val BASE_URL = "http://ec2-13-234-59-114.ap-south-1.compute.amazonaws.com:3000/api/v0/"
        //private const val BASE_URL = "http://2a914800.ngrok.io/api/v0/"

        private var okHttpClient: OkHttpClient? = null
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (okHttpClient == null) {

                initOkHttp()

            }

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient!!)
                    .build()
            }

            return retrofit!!

        }

        private fun initOkHttp() {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("package", "com.gadgetsfury.electionindia")
                    .addHeader("Content-Type", "application/json")

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            okHttpClient = httpClient.build()
        }

    }

}