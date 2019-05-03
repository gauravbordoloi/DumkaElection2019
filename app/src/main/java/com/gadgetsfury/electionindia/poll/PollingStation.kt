package com.gadgetsfury.electionindia.poll

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gadgetsfury.electionindia.util.StringListTypeConverter
import com.google.gson.annotations.SerializedName

@Entity
data class PollingStation(
    @PrimaryKey
    @NonNull
    val id: Int,
    val title: String,
    val pincode: Int?,
    val phone: String?,
    val address: String?,
    val location: String?,
    @SerializedName("blo_name")
    val bloName: String?,
    @SerializedName("images")
    @TypeConverters(StringListTypeConverter::class)
    val images: List<String>?,
    @SerializedName("no_of_voters")
    val noOfVoters: Int?,
    @SerializedName("no_of_pwd_voters")
    val noOfPwdVoters: Int?,
    @SerializedName("booth_number")
    val boothNumber: String,
    val assembly: String,
    val block: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeValue(pincode)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(location)
        parcel.writeString(bloName)
        parcel.writeStringList(images)
        parcel.writeValue(noOfVoters)
        parcel.writeValue(noOfPwdVoters)
        parcel.writeString(boothNumber)
        parcel.writeString(assembly)
        parcel.writeString(block)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PollingStation> {
        override fun createFromParcel(parcel: Parcel): PollingStation {
            return PollingStation(parcel)
        }

        override fun newArray(size: Int): Array<PollingStation?> {
            return arrayOfNulls(size)
        }
    }
}