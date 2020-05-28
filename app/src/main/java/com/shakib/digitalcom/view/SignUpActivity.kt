package com.shakib.digitalcom.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shakib.digitalcom.R
import com.shakib.digitalcom.databinding.ActivitySignUpBinding
import com.shakib.digitalcom.utils.Constants
import com.shakib.digitalcom.utils.SharedPreferencesSingleton
import com.shakib.digitalcom.utils.showToast
import com.shakib.digitalcom.utils.toggleVisibility
import com.shakib.digitalcom.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txt_mobile
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding View with ViewModels
        val binding: ActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.sAuthViewModel = authViewModel

        SharedPreferencesSingleton.init(this)

        // Calling ViewModel Functions
        authViewModel.getProceedToVerification().observe(this, Observer {
            if (it){
                Log.i("INFO", "Proceeding for verification")
                SharedPreferencesSingleton.saveString(Constants.KEY, Constants.PHONE)
                startActivity(Intent(this, VerificationActivity::class.java))
            }
        })

        authViewModel.getEtNameErrorMsg().observe(this, Observer {
            txt_username.error = it
            txt_username.requestFocus()
        })

        authViewModel.getEtDesignationErrorMsg().observe(this, Observer {
            txt_designation.error = it
            txt_designation.requestFocus()
        })

        authViewModel.getEtPhoneErrorMsg().observe(this, Observer {
            txt_mobile.error = it
            txt_mobile.requestFocus()
        })

        authViewModel.getToastMsg().observe(this, Observer {
            showToast(it)
        })

        authViewModel.getIsProgressbarVisible().observe(this, Observer {
            if (it){
                progress_bar_s.toggleVisibility()
            } else{
                progress_bar_s.toggleVisibility()
            }
        })

    }
}
