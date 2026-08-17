package com.theekshana.mediapp

import java.util.UUID

data class Medicine(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var time: String = "",
    var date: String = ""
)