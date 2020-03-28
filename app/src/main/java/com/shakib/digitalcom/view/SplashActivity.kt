package com.shakib.digitalcom.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.shakib.digitalcom.R
import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity() {
    private val mSplashDuration:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val topAnim: Animation = AnimationUtils.loadAnimation(this,
            R.anim.top_animation
        )
        img_welcome.animation = topAnim

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, mSplashDuration)
    }
}
