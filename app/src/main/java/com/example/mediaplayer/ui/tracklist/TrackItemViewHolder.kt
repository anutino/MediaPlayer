package com.example.mediaplayer.ui.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.mediaplayer.R
import com.example.mediaplayer.model.Track

@GlideModule
class TrackItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_track, parent, false)) {
    private var mArtistSong: TextView = itemView.findViewById(R.id.artist_song)
    private var mCover: ImageView = itemView.findViewById(R.id.cover)

    fun bind(track: Track) {
        mArtistSong.text = track.artist + " - " + track.trackName

        Glide
            .with(itemView)
            .load(track.cover)
            .into(mCover)
    }

}