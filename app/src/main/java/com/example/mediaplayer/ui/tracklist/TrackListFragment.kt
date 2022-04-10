package com.example.mediaplayer.ui.tracklist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackListFragment : Fragment() {
    companion object {
        private const val KEYWORD_LENGTH = 5
    }

    private val mViewModel by activityViewModels<TrackListViewModel>()
    private lateinit var mSearchField: EditText
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: TrackListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_track_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchField = view.findViewById(R.id.search)
        mSearchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.length >= KEYWORD_LENGTH) {
                    mViewModel.loadTrackList(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        mAdapter = TrackListAdapter(object : TrackListAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                mViewModel.selectedItemId(itemId)
                (activity as MainActivity).showPlayerFragment()
            }
        })
        mRecyclerView = view.findViewById(R.id.track_list)

        val dividerItemDecoration = DividerItemDecoration(mRecyclerView.context,
            DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(dividerItemDecoration)

        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mViewModel.trackList.observe(viewLifecycleOwner) {
            mAdapter.setList(it)
            mAdapter.notifyDataSetChanged()
        }
    }

}