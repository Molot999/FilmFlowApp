package com.blacksun.filmflow.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Locale
import android.os.Parcel;

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("localized_name") val localizedName: String,
    @SerializedName("name") val originalName: String,
    @SerializedName("year") val year: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("description") val description: String,
    @SerializedName("genres") val genres: List<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        localizedName = parcel.readString() ?: "",
        originalName = parcel.readString() ?: "",
        year = parcel.readInt(),
        rating = parcel.readFloat(),
        imageUrl = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        genres = parcel.createStringArrayList()?.toList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(localizedName)
        parcel.writeString(originalName)
        parcel.writeInt(year)
        parcel.writeFloat(rating)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
        parcel.writeStringList(genres)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

}