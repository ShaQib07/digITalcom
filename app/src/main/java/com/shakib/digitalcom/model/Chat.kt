package com.shakib.digitalcom.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Chat (
    val chat: String? = "")
