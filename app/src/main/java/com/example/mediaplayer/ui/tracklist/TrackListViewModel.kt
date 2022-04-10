package com.example.mediaplayer.ui.tracklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaplayer.data.repository.MusicRepositoryImpl
import com.example.mediaplayer.model.Track
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class TrackListViewModel @Inject constructor(repository: MusicRepositoryImpl) : ViewModel() {

    var compositeDisposable = CompositeDisposable()

    private val repository: MusicRepositoryImpl = repository
    private val trackListLiveData = MutableLiveData<List<Track>>()
    val trackList = trackListLiveData

    private val mutableSelectedItem = MutableLiveData<Track>()
    val selectedItem: LiveData<Track> get() = mutableSelectedItem

    fun loadTrackList(keyword: String) {
        val result: MutableList<Track> = mutableListOf()
        val disposable = repository.getTrackList(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it))
                    val jsonArray = jsonObject.getJSONArray("results")
                    for (i in 0 until jsonArray.length()) {
                        val tt: JSONObject = jsonArray.getJSONObject(i)
                        val track = Track(
                            tt.getString("trackId").toInt(),
                            tt.getString("artistName"),
                            tt.getString("trackName"),
                            tt.getString("artworkUrl100"),
                            tt.getString("previewUrl")
                        )
                        result.add(track)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                trackListLiveData.postValue(result)
            }
        compositeDisposable.add(disposable)
    }

    fun selectedItemId(item: Int) {
        for (i in trackListLiveData.value!!) {
            if (i.id == item) {
                mutableSelectedItem.postValue(i)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun selectedTestTrackItem() {
        val track =
            Track(1444851002,
                "Artists Stand Up to Cancer",
                "Just Stand Up! (feat. Mariah, Beyoncé,",
                "https://is4-ssl.mzstatic.com/image/thumb/Music124/v4/b8/76/43/b8764322-cf61-5308-6499-6366859e3824/source/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/de/25/b6/de25b66e-6041-1c43-70ad-1f55fcb11364/mzaf_11383194160370561048.plus.aac.p.m4a")
        mutableSelectedItem.postValue(track)
    }

}