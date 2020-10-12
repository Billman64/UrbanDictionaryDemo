package com.github.billman64.urbandictionarydemo.Model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

interface ApiService{

    @Headers(value = ["x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com",
        "x-rapidapi-key: insertApiKey" ])     //TODO: hide api key
    @GET("define")
    fun getWords(@Query("term") searchTerm:String): Call<JsonObject>

    //    fun getWords(): Call<Array<WordItem>>
    //    suspend fun getWords(): Response<List<WordItem>>
}