package com.example.mediaplayer.data.repository

import com.google.gson.JsonObject
import io.reactivex.Observable

interface MusicRepository {
    fun getTrackList(keyword: String): Observable<JsonObject>
}