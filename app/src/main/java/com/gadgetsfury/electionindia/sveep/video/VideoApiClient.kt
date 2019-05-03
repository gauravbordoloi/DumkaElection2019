package com.gadgetsfury.electionindia.sveep.video

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class VideoApiClient {

    companion object {

        private const val CONNECT_TIMEOUT = 30.toLong()
        private const val READ_TIMEOUT = 30.toLong()
        private const val WRITE_TIMEOUT = 30.toLong()

        private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
        const val YOUTUBE_API = "AIzaSyAD6boU5XDfVlDpF110Rctd-_ljsM0ZJPk"
        const val CHANNEL_ID = "UCGdsgdPfwnwYF1l8YTFkknA"

        private var okHttpClient: OkHttpClient? = null
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (okHttpClient == null) {

                initOkHttp()

            }

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
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

            /*httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("cache-control", "no-cache")
                    //.header("Authorization", "Bearer " + AppController.getSharedPref().getFirebaseIdToken())
                    .addHeader("Content-Type", "application/json")

                val request = requestBuilder.build()
                chain.proceed(request)
            }*/

            okHttpClient = httpClient.build()
        }

    }

}