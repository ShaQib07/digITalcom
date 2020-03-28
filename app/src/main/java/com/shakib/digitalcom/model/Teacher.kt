package com.shakib.digitalcom.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Teacher (
    val name: String? = "",
    val designation: String? = "",
    val number: String? = "")
