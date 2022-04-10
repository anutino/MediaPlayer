package com.example.mediaplayer.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.mediaplayer.R
import com.example.mediaplayer.ui.tracklist.TrackListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private val mViewModel by activityViewModels<TrackListViewModel>()
    private lateinit var mCover: ImageView
    private lateinit var mTrackName: TextView
    private lateinit var mArtistName: TextView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mPosition: TextView
    private lateinit var mDuration: TextView
    private lateinit var mPlayPauseView: PlayPauseView
    private lateinit var mUri: String
    private lateinit var mService: MusicService
    private var mIsPlaying = false
    private var mIsBound = false

    private var mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as MusicService.MyLocalBinder
            mService = binder.getService()
            mIsBound = true
            initLiveData()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mIsBound = false
        }
    }

    private fun initLiveData() {
        mService.position.observe(viewLifecycleOwner) { mPosition.text = it }
        mService.currentDuration.observe(viewLifecycleOwner) { mDuration.text = it }
        mService.seekBar.observe(viewLifecycleOwner) {
            mSeekBar.progress = it
            if (mSeekBar.max == 0) {
                mSeekBar.max = it
            }
        }
        mService.stopPlayer.observe(viewLifecycleOwner) { mPlayPauseView.setState(false) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCover = view.findViewById(R.id.cover)
        mTrackName = view.findViewById(R.id.track_name)
        mArtistName = view.findViewById(R.id.artist_name)
        mSeekBar = view.findViewById(R.id.seekbar)
        mPosition = view.findViewById(R.id.position)
        mDuration = view.findViewById(R.id.duration)
        mPlayPauseView = view.findViewById(R.id.playPause)
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (mIsBound) {
                    mService.updatePlayingTime();
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (mIsBound) {
                    mService.seekMediaPlayer(mSeekBar.progress);
                }
            }
        })

        mViewModel.selectedItem.observe(viewLifecycleOwner) {
            Glide
                .with(mCover)
                .load(it.cover)
                .into(mCover)
            mTrackName.text = it.trackName
            mArtistName.text = it.artist
            mUri = it.previewUrl
            bindService()
        }
        mPlayPauseView.setOnClickListener {
            mPlayPauseView.setState(mIsPlaying)
            mIsPlaying = !mIsPlaying
            playPause()
        }
        mViewModel.selectedItemId(1)
    }

    override fun onStop() {
        super.onStop()
        if (mIsBound) {
            requireActivity().unbindService(mServiceConnection)
            mIsBound = false
        }
    }

    private fun bindService() {
        if (!mIsBound) {
            val intent = Intent(activity, MusicService::class.java)
            intent.data = Uri.parse(mUri)
            requireActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
            requireActivity().startService(intent)
        }
    }

    private fun playPause() {
        if (mIsBound) {
            mService.playPause();
        }
    }

}