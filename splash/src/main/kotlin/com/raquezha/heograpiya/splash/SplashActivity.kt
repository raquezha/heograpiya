package com.raquezha.heograpiya.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowInsets.Side.all
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.appcompat.app.AppCompatActivity
import com.raquezha.heograpiya.shared.contentView
import com.raquezha.heograpiya.splash.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity() {

    private val binding: ActivitySplashBinding by contentView(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
80
        enableFullScreenMode()

        actionBar?.hide()

        binding.lottie.clearAnimation()
        binding.lottie.setAnimationFromUrl(
            "https://assets5.lottiefiles.com/packages/lf20_GATegF.json",
            "splash1"
        )
        binding.lottie.setFailureListener {

        }
        binding.lottie.playAnimation()
    }

    @Suppress("DEPRECATION")
    private fun enableFullScreenMode() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

            val controller = window.insetsController!!

            // Immersive is now...
            controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            controller.hide(all())
            controller.setSystemBarsAppearance(0,0)
        }
        else -> {
            window.addFlags(
                SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        }
    }
}