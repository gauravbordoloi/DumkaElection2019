package com.gadgetsfury.electionindia.sveep.rohof

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ROHoF(
    @PrimaryKey
    @NonNull
    val id: Int,
    val name: String,
    val image: String,
    val timestamp: Long
)