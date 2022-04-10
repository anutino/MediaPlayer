package com.example.mediaplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.mediaplayer.R
import com.example.mediaplayer.ui.player.PlayerFragment
import com.example.mediaplayer.ui.tracklist.TrackListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            add(R.id.fragment_container_view, TrackListFragment())
        }
    }

    fun showPlayerFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, PlayerFragment())
            addToBackStack(TrackListFragment.javaClass.name)
        }
    }

}