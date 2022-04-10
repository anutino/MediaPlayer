package com.example.mediaplayer.data.repository

import com.example.mediaplayer.retrofit.NetworkService
import com.google.gson.JsonObject
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(service: NetworkService) : MusicRepository {

    private var mService: NetworkService = service

    override fun getTrackList(keyword: String): Observable<JsonObject> {
        return mService.getRetrofitService().getTrackList(keyword)
    }

}