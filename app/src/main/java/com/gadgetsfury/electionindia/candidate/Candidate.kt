package com.gadgetsfury.electionindia.candidate

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Candidate(
    @PrimaryKey
    @NonNull
    val id: Int,
    val name: String,
    val district: String,
    val image: String,
    val symbol: String
)