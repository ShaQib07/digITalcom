package com.shakib.digitalcom.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shakib.digitalcom.R
import com.shakib.digitalcom.databinding.ActivityVerificationBinding
import com.shakib.digitalcom.utils.showToast
import com.shakib.digitalcom.utils.startHomeActivity
import com.shakib.digitalcom.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding View with ViewModels
        val binding: ActivityVerificationBinding = DataBindingUtil.setContentView(this, R.layout.activity_verification)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.vAuthViewModel = authViewModel

        btn_back.setOnClickListener {onBackPressed()}


        // Calling ViewModel Functions
        authViewModel.sendVerificationCode()

        authViewModel.getProceedToHome().observe(this, Observer {
            if (it){
                Log.i("INFO", "Proceeding for Home")
                startHomeActivity()
            } else{
                Log.i("INFO", "Proceeding for Home else")
            }
        })

        authViewModel.getEtOtpCode().observe(this, Observer {
            et_otp.setText(it)
            Log.i("INFO", "OTP code changed")
        })

        authViewModel.getEtOtpErrorMsg().observe(this, Observer {
            et_otp.error = it
            et_otp.requestFocus()
            Log.i("INFO", "Showing otp error msg in verification activity")
        })

        authViewModel.getToastMsg().observe(this, Observer {
            showToast(it)
            Log.i("INFO", "Showing toast in verification activity")
        })

    }
}

