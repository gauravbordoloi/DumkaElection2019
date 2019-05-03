package com.gadgetsfury.electionindia.sveep.vfeed

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VoterFeed(
    @PrimaryKey
    @NonNull
    val id: Int,
    val name: String?,
    val image: String,
    val timestamp: Long,
    val content: String?
)