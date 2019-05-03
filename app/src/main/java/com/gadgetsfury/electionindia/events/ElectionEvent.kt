package com.gadgetsfury.electionindia.events

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ElectionEvent(
    @PrimaryKey
    @NonNull
    val id: Int,
    val name: String,
    val description: String,
    val link: String,
    @SerializedName("start_timestamp")
    val startTimestamp: Long,
    @SerializedName("end_timestamp")
    val endTimestamp: Long,
    val interested: Int,
    @SerializedName("not_interested")
    val notInterested: Int,
    var isInterested: Int //0 -> null 1-> interested, 2-> not interested
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(link)
        parcel.writeLong(startTimestamp)
        parcel.writeLong(endTimestamp)
        parcel.writeInt(interested)
        parcel.writeInt(notInterested)
        parcel.writeInt(isInterested)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ElectionEvent> {
        override fun createFromParcel(parcel: Parcel): ElectionEvent {
            return ElectionEvent(parcel)
        }

        override fun newArray(size: Int): Array<ElectionEvent?> {
            return arrayOfNulls(size)
        }
    }
}