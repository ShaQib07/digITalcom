package com.shakib.digitalcom.utils

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.shakib.digitalcom.R
import com.shakib.digitalcom.view.HomeActivity
import com.shakib.digitalcom.view.LoginActivity

fun Context.showToast(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 100)
    val view:View = toast.view

    //To change the Background of Toast
    view.background
        .setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
    /*val text:TextView = view.findViewById(R.id.message)
    text.setTextColor(Color.WHITE)
    text.textSize = 18f*/
    toast.show()
}

fun View.toggleVisibility() {
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