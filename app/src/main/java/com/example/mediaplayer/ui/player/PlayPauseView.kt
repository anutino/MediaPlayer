package com.example.mediaplayer.ui.player

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

class PlayPauseView : AppCompatImageView {

    private lateinit var mAvd: AnimatedVectorDrawable
    private lateinit var mAvdc: AnimatedVectorDrawableCompat

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    private fun startAnim() {
        val drawable = drawable
        if (drawable is AnimatedVectorDrawableCompat) {
            mAvdc = drawable
            mAvdc.start()
        } else if (drawable is AnimatedVectorDrawable) {
            mAvd = drawable
            mAvd.start()
        }
    }

    fun setState(isPlay: Boolean) {
        if (isPlay) {
            setImageDrawable(
                context.getDrawable(com.example.mediaplayer.R.drawable.pause_to_play))
        } else {
            setImageDrawable(
                context.getDrawable(com.example.mediaplayer.R.drawable.play_to_pause))
        }
        startAnim()
    }

}
