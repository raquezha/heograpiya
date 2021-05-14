package com.raquezha.heograpiya.shared

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("layoutFullscreen")
fun View.bindLayoutFullscreen(previousFullscreen: Boolean, fullscreen: Boolean) {
    if (previousFullscreen != fullscreen && fullscreen) {
        @Suppress("DEPRECATION")
        // The new alternative is WindowCompat.setDecorFitsSystemWindows, but we can't
        // always get access to the window from a view.
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}