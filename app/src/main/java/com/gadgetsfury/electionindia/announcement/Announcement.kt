package com.gadgetsfury.electionindia.announcement

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Announcement(
    @PrimaryKey
    @NonNull
    val id: Int,
    val title: String,
    val timestamp: Long
)