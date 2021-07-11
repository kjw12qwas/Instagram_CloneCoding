package com.example.jaewonstagram.model.domain

import java.time.LocalDateTime
import java.util.*

data class ListPictureResponse(
    val picture_id : Int,
    val user_id: Int,
    val image: String,
    val filename: String,
    val description: String,
    val created_at: LocalDateTime
)
