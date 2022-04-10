package com.example.mediaplayer.model

import com.google.gson.annotations.SerializedName

data class Track(@SerializedName("trackId") val id: Int,
                 @SerializedName("artistName") val artist: String,
                 @SerializedName("trackName") val trackName: String,
                 @SerializedName("artworkUrl30") val cover: String,
                 @SerializedName("previewUrl") val previewUrl: String)

