package com.github.billman64.urbandictionarydemo.Model

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import java.lang.reflect.Type

interface ApiService{

    @Headers(value = ["x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com",
        "x-rapidapi-key: <insertApiKeyHere>" ])     //TODO: hide api key
    @GET("define?term=wat")     //TODO: implement variable for term
    fun getWords(): Call<JsonObject>

    //    fun getWords(): Call<Array<WordItem>>
    //    suspend fun getWords(): Response<List<WordItem>>
}