package com.theekshana.mediapp

import java.util.UUID

data class Appointment(
    val id: String = UUID.randomUUID().toString(),
    val doctorName: String = "",
    val doctorSpecialty: String = "",
    val date: String = "",
    val time: String = ""
)
