package com.shakib.digitalcom.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shakib.digitalcom.R
import com.shakib.digitalcom.databinding.ActivityMainBinding
import com.shakib.digitalcom.utils.Constants
import com.shakib.digitalcom.utils.SharedPreferencesSingleton
import com.shakib.digitalcom.utils.startHomeActivity
import com.shakib.digitalcom.utils.startLoginActivity
import com.shakib.digitalcom.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private val mSplashDuration:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Binding View with ViewModels
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.sAuthViewModel = authViewModel

        SharedPreferencesSingleton.init(this)

        val topAnim: Animation = AnimationUtils.loadAnimation(this,
            R.anim.top_animation
        )
        img_welcome.animation = topAnim

        Handler().postDelayed({
            if (checkForUser()){
                startHomeActivity()
            } else{
                startLoginActivity()
            }

        }, mSplashDuration)
    }

    private fun checkForUser(): Boolean {
        return if (SharedPreferencesSingleton.getBoolean(Constants.IS_VERIFIED_USER)){
            Constants.PHONE = SharedPreferencesSingleton.getString(Constants.KEY)!!
            authViewModel.getSavedUser()
            true
        } else{
            false
        }
    }
}
