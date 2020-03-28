package com.shakib.digitalcom.utils

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.shakib.digitalcom.view.HomeActivity
import com.shakib.digitalcom.view.LoginActivity

fun Context.showToast(msg: String): Unit{
    val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show()
}

fun View.toggleVisibility(): Unit{
    visibility = if(visibility == View.VISIBLE)
        View.INVISIBLE
    else
        View.VISIBLE
}

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }