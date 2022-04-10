package com.example.mediaplayer.retrofit

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor() {
    private val mRetrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)

    fun getRetrofitService(): RetrofitServices {
        return mRetrofitService
    }

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
    }
}