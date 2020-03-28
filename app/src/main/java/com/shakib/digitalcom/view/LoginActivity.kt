package com.shakib.digitalcom.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shakib.digitalcom.R
import com.shakib.digitalcom.databinding.ActivityLoginBinding
import com.shakib.digitalcom.utils.showToast
import com.shakib.digitalcom.utils.toggleVisibility
import com.shakib.digitalcom.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding View with ViewModels
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.lAuthViewModel = authViewModel


        // Calling ViewModel Functions
        authViewModel.getOpenSignUp().observe(this, Observer {
            if (it){
                val pairs = arrayOfNulls<Pair<View, String>>(5)
                pairs[0] = Pair(txt_title, "title_text")
                pairs[1] = Pair(txt_sub_title, "sub_title_text")
                pairs[2] = Pair(field_mobile, "mobile_field")
                pairs[3] = Pair(txt_otp_caution, "otp_text")
                pairs[4] = Pair(btn_send_otp, "otp_btn")
                val options = ActivityOptions.makeSceneTransitionAnimation(this, *pairs)
                startActivity(Intent(this, SignUpActivity::class.java), options.toBundle())
            }
        })

        authViewModel.getProceedToVerification().observe(this, Observer {
            if (it){
                Log.i("INFO", "Proceeding for verification")
                startActivity(Intent(this, VerificationActivity::class.java))
            }
        })

        authViewModel.getEtPhoneErrorMsg().observe(this, Observer {
            txt_mobile.error = it
            txt_mobile.requestFocus()
        })

        authViewModel.getToastMsg().observe(this, Observer {
            showToast(it)
            Log.i("INFO", "Showing toast in login activity")
        })

        authViewModel.getIsProgressbarVisible().observe(this, Observer {
            if (it){
                Log.i("INFO", "Inside Progressbar visibility true: $it")
                progress_bar_l.toggleVisibility()
            } else{
                Log.i("INFO", "Inside Progressbar visibility false: $it")
                progress_bar_l.toggleVisibility()
            }
        })
    }
}
