package com.github.billman64.urbandictionarydemo.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JsonFeed(val jsonArr: JsonArray){

    @SerializedName("list")
    @Expose
//    private var list:Gson = list.fromJson(
    private val list:JsonArray = jsonArr

}