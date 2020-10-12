package com.github.billman64.urbandictionarydemo.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// word poko

data class WordItem(var word:String, var definition:String, var thumbsUp:Int, var thumbsDown:Int) :
    Parcelable {

    @SerializedName("word")
    @Expose
    var mWord:String
        internal set
    @SerializedName("definition")
    @Expose
    var mDefinition:String
        internal set
    @SerializedName("thumbs_up")
    @Expose
    var mThumbsUp:Int
        internal set
    @SerializedName("thumbs_down")
    @Expose
    var mThumbsDown:Int
        internal set

    constructor(parcel: Parcel) : this(
        parcel.readString()?:"no string found",
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt()
    )

    init{
        mWord = word
        mDefinition = definition
        mThumbsUp = thumbsUp
        mThumbsDown = thumbsDown
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(word)
        parcel.writeString(definition)
        parcel.writeInt(thumbsUp)
        parcel.writeInt(thumbsDown)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WordItem> {
        override fun createFromParcel(parcel: Parcel): WordItem {
            return WordItem(parcel)
        }

        override fun newArray(size: Int): Array<WordItem?> {
            return arrayOfNulls(size)
        }
    }

}

