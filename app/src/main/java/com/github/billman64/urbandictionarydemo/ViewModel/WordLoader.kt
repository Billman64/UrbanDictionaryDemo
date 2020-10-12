package com.github.billman64.urbandictionarydemo.ViewModel

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.github.billman64.urbandictionarydemo.Model.WordItem
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.ArrayList


class WordLoader(context: Context, url: URL):AsyncTaskLoader<List<WordItem>>(context){
    val TAG:String = "Nike demo"
    internal var mUrl:String? = null

    init{
        mUrl = url.toString()
    }

    override fun onStartLoading() {
        Log.d(TAG, "AsyncTaskLoader onStartLoading().")
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): List<WordItem>? {
        Log.d(TAG, "AsyncTaskLoader loadInBackground().")

        if (mUrl == null) return null

        val hh = HTTPHandler()

        try {
            // make request to the endpoint's URL
            val rawJson = hh.startHttpRequest(URL(mUrl))
            if (rawJson == null) {
                Log.d(TAG, "HTTPHandler done. rawJson is null")
                return null
            } else {
                Log.d(TAG, "HTTPHandler done. rawJson: " + rawJson.substring(0, 50) + "...")


                val jsonRoot = JSONObject(rawJson)
                Log.d(TAG, "jsonRoot: " + jsonRoot.toString().substring(0, 100) + "...")

                // create JSON objects to traverse to general data area
                val jsonObjectResponse = jsonRoot.optJSONObject("response")
                Log.d(TAG, "jsonObjectResponse: " + jsonObjectResponse.toString().substring(0, 100))
                val jsonArrayResults = jsonObjectResponse.getJSONArray("results")
                Log.d(TAG, "jsonObjectResponse created with length: " + jsonObjectResponse.length())

                // create a temporary WordItem list to return
                val wordItemList = ArrayList<WordItem>()

                // loop through records
                for (i in 0 until jsonArrayResults.length()) {
                    // create individual JSON objects and arrays to traverse to specific data points
                    val j = jsonArrayResults.getJSONObject(i)
                    val word = j.optString("word")
                    val definition = j.optString("definition")
                    val thumbsUp = j.optInt("thumbs_up")
                    val thumbsDown = j.optInt("thumbs_down")

                    val jsonArrayTags = j.optJSONArray("tags")
//                    val jsonObjectTag = jsonArrayTags.optJSONObject(0)


                    Log.d(TAG, "word found: $word")    // it could be fun or funny to see an UD term in a logcat log


                    // put each JSON record into the WordItem list
                    wordItemList.add(WordItem(word, definition, thumbsUp, thumbsDown))
                }
                return wordItemList
            }

        } catch (e: Exception) {
            Log.e(TAG, "Parse, or other, exception: " + e.message?.substring(0, 100))

            //TODO: differentiate between different exceptions, such as malformedURL or ParseException

            return null
        }

    }

}