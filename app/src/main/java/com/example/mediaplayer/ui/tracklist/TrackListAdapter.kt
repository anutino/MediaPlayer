package com.example.mediaplayer.ui.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.model.Track

class TrackListAdapter(listener: OnItemClickListener) :
    RecyclerView.Adapter<TrackItemViewHolder>() {

    private var mTrackList: List<Track> = mutableListOf()
    private var mOnItemClickListener = listener

    interface OnItemClickListener {
        fun onItemClick(itemId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackItemViewHolder {
        return TrackItemViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: TrackItemViewHolder, position: Int) {
        holder.bind(mTrackList[position])
        holder.itemView.setOnClickListener {
            mOnItemClickListener.onItemClick(mTrackList[position].id)
        }
    }

    fun setList(list: List<Track>) {
        mTrackList = list
    }

    override fun getItemCount(): Int {
        return mTrackList.size
    }

}