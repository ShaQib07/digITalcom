package com.shakib.digitalcom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.shakib.digitalcom.repository.TeacherRepository
import com.shakib.digitalcom.utils.Constants
import com.shakib.digitalcom.utils.Constants.PHONE
import java.util.concurrent.TimeUnit

class AuthViewModel: ViewModel() {

    var phoneNumber:String? = null
    var userName:String? = null
    var designation:String? = null
    var otp:String? = null
    var isFromSignUp = false
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val teachersRepository =TeacherRepository.getInstance()
    private val openSignUp = MutableLiveData<Boolean>()
    private val proceedToVerification = MutableLiveData<Boolean>()
    private val etPhoneErrorMsg = MutableLiveData<String>()
    private val etNameErrorMsg = MutableLiveData<String>()
    private val etDesignationErrorMsg = MutableLiveData<String>()
    private val etOtpErrorMsg = MutableLiveData<String>()
    private val toastMsg = MutableLiveData<String>()
    private val isProgressbarVisible = MutableLiveData<Boolean>()
    private val proceedToHome = MutableLiveData<Boolean>()
    private val etOtpCode = MutableLiveData<String>()

    // Login Functions
    fun onSendOtpButtonClick(){
        isProgressbarVisible.value = true
        if (phoneNumber.isNullOrEmpty() || phoneNumber?.length != 11){
            etPhoneErrorMsg.value = "Please enter a valid number"
            isProgressbarVisible.value = false
        } else {
            teachersRepository.teacherExists("+88$phoneNumber").observeForever {
                if (it){
                    proceedToVerification.value = true
                    isProgressbarVisible.value = false
                    PHONE += "+88$phoneNumber"
                } else{
                    Log.i("INFO", "Inside view model method: ${teachersRepository.getTeachers().value}")
                    toastMsg.value = "Oops! This number is not registered, Sign Up first"
                    isProgressbarVisible.value = false
                }
            }
        }
    }

    fun onNewUserClick(){
        openSignUp.value = true
    }

    // SignUp Functions
    fun onSignUpSendOtpButtonClick(){
        isProgressbarVisible.value = true
         if(userName.isNullOrEmpty()){
            etNameErrorMsg.value = "Please enter your name"
             isProgressbarVisible.value = false
        } else if(designation.isNullOrEmpty()){
            etDesignationErrorMsg.value = "Please enter your designation"
             isProgressbarVisible.value = false
        } else if (phoneNumber.isNullOrEmpty() || phoneNumber?.length != 11){
             etPhoneErrorMsg.value = "Please enter a valid number"
             isProgressbarVisible.value = false
         } else {teachersRepository.teacherExists("+88$phoneNumber").observeForever {
             if (it){
                 toastMsg.value = "Oops! This number is already registered, Try  Log In"
                 isProgressbarVisible.value = false
             } else{
                 proceedToVerification.value = true
                 isProgressbarVisible.value = false
                 PHONE += "+88$phoneNumber"
                 isFromSignUp = true
             }
         }
        }
    }

    // Verification Functions
    fun onSignInButtonClick(){
        if (otp.isNullOrEmpty() || otp?.length != 6) {
            etOtpErrorMsg.value = "Invalid code"
        } else{
            verifyCode(otp!!)
        }

    }

    fun sendVerificationCode() {
        Log.i("INFO", "Sending verification code")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            PHONE!!,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                Log.i("INFO", "Inside onCodeSent: $s")
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    etOtpCode.value = code
                    verifyCode(code)
                    Log.i("INFO", "Inside onVerificationCompleted")
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                toastMsg.value = e.message
                Log.i("INFO", "Inside onVerificationFailed: ${toastMsg.value}")
            }
        }

    private fun verifyCode(code: String) {
        Log.i("INFO", "Inside verifyCode")
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, code) }
        signInWithCredential(credential!!)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        Log.i("INFO", "Inside signInWithCredential")
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (isFromSignUp){
                        teachersRepository.writeNewUser(userName!!, designation!!, phoneNumber!!)
                    }
                    proceedToHome.value = true
                    Log.i("INFO", "proceedToHome: ${proceedToHome.value}")
                } else {
                    toastMsg.value = task.exception?.message
                }
            }
    }


    // ViewModel Functions
    fun getOpenSignUp(): LiveData<Boolean> = openSignUp

    fun getProceedToVerification(): LiveData<Boolean> = proceedToVerification

    fun getEtNameErrorMsg(): LiveData<String> = etNameErrorMsg

    fun getEtDesignationErrorMsg(): LiveData<String> = etDesignationErrorMsg

    fun getEtPhoneErrorMsg(): LiveData<String> = etPhoneErrorMsg

    fun getToastMsg(): LiveData<String> = toastMsg

    fun getIsProgressbarVisible(): LiveData<Boolean> = isProgressbarVisible

    fun getEtOtpErrorMsg(): LiveData<String> = etOtpErrorMsg

    fun getEtOtpCode(): LiveData<String> = etOtpCode

    fun getProceedToHome(): LiveData<Boolean> = proceedToHome
}