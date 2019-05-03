package com.gadgetsfury.electionindia.sveep.vod

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class VOD(
    @PrimaryKey
    @NonNull
    val id: Int,
    val name: String,
    val image: String,
    val timestamp: Long,
    val content: String?
)