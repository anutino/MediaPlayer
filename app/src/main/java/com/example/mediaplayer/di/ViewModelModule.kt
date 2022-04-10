package com.example.mediaplayer.di

import androidx.lifecycle.ViewModel
import com.example.mediaplayer.ui.tracklist.TrackListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindTrackListViewModule(viewModelModule: TrackListViewModel): ViewModel
}