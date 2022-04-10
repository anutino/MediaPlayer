package com.example.mediaplayer.retrofit

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("search")
    fun getTrackList(@Query("term") term: String): Observable<JsonObject>

}