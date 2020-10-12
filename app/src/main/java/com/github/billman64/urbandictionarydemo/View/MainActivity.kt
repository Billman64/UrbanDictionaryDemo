/* Urban Dictionary API call demo for Nike
    @author: Bill Lugo

    Copyrights:
        Data source: Urban Dictionary (UrbanDictionary.com)
        Background photo by Devon Delrio is licensed under Creative Commons CC0 (Public Domain). Source: https://pixy.org/107734/
 */

package com.github.billman64.urbandictionarydemo.View

import android.content.res.ColorStateList
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.billman64.urbandictionarydemo.Model.ApiService
import com.github.billman64.urbandictionarydemo.Model.WordAdapter
import com.github.billman64.urbandictionarydemo.Model.WordItem
import com.github.billman64.urbandictionarydemo.R
import com.google.gson.JsonArray
//import com.github.billman64.urbandictionarydemo.ViewModel.RetrofitClient.apiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONArray
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * The main activity for this app.
 * @author: Bill Lugo
 * @Desc: Searches Urban Dictionary for a term, via an API call.
 */
class MainActivity : AppCompatActivity() {
    val TAG:String="Nike demo"
    private var list = ArrayList<WordItem>()
    private val mockData = false     // only for testing purposes
    private var sort:String?=""
    enum class Sorting {
        THUMBS_UP, THUMBS_DOWN
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up recyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WordAdapter(ArrayList())

        // ViewModel provider
//        var vm = ViewModelProviders.of(this)
//        var a: LiveData

        // Log whether or not retrieving data from a prior activity instance.
        savedInstanceState?.let{Log.d(TAG, "onCreate() with a savedInstanceState")} ?: Log.d(TAG, "onCreate()")

        // Button functionality - initiate API call
        val button:Button = searchButton

        button.setOnClickListener{
            Log.d(TAG, "Button pressed.")

            // hide soft keyboard
            searchTerm.onEditorAction(EditorInfo.IME_ACTION_DONE)   //TODO: hide keyboard after Go key press

            // get search term
            var searchTermValue:String = searchTerm.text.toString()

            // reset list data
            list.clear()

            // Get new data - API call or mock data (if enabled)
            if(!mockData && searchTermValue.isNotBlank()) {
                // API network call

                // show progressBar
                progress_bar.visibility = View.VISIBLE
                progress_bar.isShown

                // Retrofit builder
                val api = Retrofit.Builder()
//                    .baseUrl(getString(R.string.api_url))
                    .baseUrl("https://mashape-community-urban-dictionary.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
                Log.d(TAG," retrofit created. toString(): $api.toString()")


                // API call via the Retrofit in a coroutine
                GlobalScope.launch(Dispatchers.IO){
                    Log.d(TAG, " coroutine")

                    try {
                        val response = api.getWords(searchTermValue).awaitResponse()      //TODO: ERROR - null object reference

                        Log.d(TAG, " api.getWords(). Response success:${response.isSuccessful} body: " + response.body().toString().substring(0,100))

                        if (response.isSuccessful) {
                            //TODO: put successfully-found data in a LiveData object that will update views

                            Log.d(TAG, " response is successful! code+msg: " + response.code() +" "+ response.message())
                            Log.d(TAG, " response is successful! body: " + response.body().toString().substring(0,100))

//                            val rawJson:String = response.body().toString()
//                            Log.d(TAG," rawJson: " + rawJson.substring(0,80))


//                            val data = JsonArray().add(rawJson)
                             val data = response.body()!!.getAsJsonArray("list")
                            // val data = JSONArray(rawJson) // response.body()!!  // resolved error - org.json.JSONException: Value {"
//                            Log.d(TAG, " data response successful! Length of data: " + data.size())
//                            Log.d(TAG, " data response successful! Length of data: " + data.length())
                            Log.d(TAG, " data response successful! Length of data: " + data.toString().substring(0,100))


                            // add each word to list, before updating RecyclerView
                            for(i in 0 until data.size()){
//                            for(i in 0 until data.length()){
                                var w = WordItem(
//                                    data.getJSONObject(i).get("word").toString(),

                                    data[i].asJsonObject.get("word").toString(),
                                    data[i].asJsonObject.get("definition").toString().replace("\\r", " ").replace("\\n", " "),
                                        //TODO: .replace \" with " (regex?, ascii char. #34?)
                                    data[i].asJsonObject.get("thumbs_up").asInt,
                                    data[i].asJsonObject.get("thumbs_down").asInt
                                )
                                list.add(w)
                            }
                            Log.d(TAG, " new list count: ${list.count()}")


                            // update UI - via dispatcher to main thread
                            withContext(Dispatchers.Main) {
                                progress_bar.visibility = View.GONE

                                recyclerView.adapter =
                                    WordAdapter(list)    // update recyclerView

                                Log.d(TAG, " Adapter count: " + WordAdapter(list).itemCount)

                            }


                            // Reset sort thumbs color
                            sort_thumbs_up.setColorFilter(
                                ContextCompat.getColor(
                                    baseContext,
                                    R.color.thumb
                                )
                            )
                            sort_thumbs_down.setColorFilter(
                                ContextCompat.getColor(
                                    baseContext,
                                    R.color.thumb
                                )
                            )
                        } else {

                            //TODO: if not successful, display an error
                            Log.d(
                                TAG,
                                " Network call not successful. Response body(): " + response.body()
                            )
                            Log.d(TAG, " Response code(): " + response.code())
                            Log.d(TAG, " Response message(): " + response.message().toString())
                            Log.d(
                                TAG, " Response " +
                                        "errorBody(): " + response.errorBody()
                            )

                            // update UI - via dispatcher to main thread
                            withContext(Dispatchers.Main) {
                                progress_bar.visibility = View.GONE
                            }

                        }
                    } catch(e:Exception){
                        Log.d(TAG, " Net call exception: ${e.toString().subSequence(0,100)}")


                        // update UI - via dispatcher to main thread
                        withContext(Dispatchers.Main) {
                            progress_bar.visibility = View.GONE
                        }

                    }
                }

            } else {        // mock data offline, if enabled (only for UI/UX testing)
                mockData()
            }

            list.let { if(list.isNotEmpty()) enableSortBar() }   // enable sort bar if list has data
        }
    }

    /**
     *  onSaveInstanceState preserves on-screen data in preparation for orientation change (device rotation).
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d(TAG, "onSaveInstanceState()")

        // Gather recyclerView data via the list ArrayList<>
        val saveList = list
        Log.d(TAG," adapter count: "+ WordAdapter(saveList).itemCount)

        // Pass the data on for the next activity instance.
        outState.putParcelableArrayList("list", saveList)

        // test code - make sure data is retrievable
        val testList:ArrayList<WordItem>? = outState.getParcelableArrayList("list") // test outState list
        Log.d(TAG," getParcelable list count: ${testList?.count()}")

        // save sortBar status
        if(sortBar.visibility == View.VISIBLE){
            outState.putBoolean("sortBar",true)
            sort?.let { outState.putString("sort", sort)
                Log.d(TAG," preserving sort setting: $sort")}
        }
    }

    /**
     * onRestoreInstanceState recovers visible data saved from onSavedInstanceState, bringing a seamless user experience after orientation change.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.d(TAG, "onRestoreInstanceState")

        // Gather data from savedInstanceState (passed from previous activity instance)
        val restoreList:ArrayList<WordItem>? = savedInstanceState.getParcelableArrayList<WordItem>("list")
        Log.d(TAG, " restoreList count: "+ restoreList?.count())

        // If there is data, update the recyclerView (through an adapter).
        restoreList?.let{
            list = restoreList      // Restore original list listArray, so that future operations on it will also be updated
            val adapter = WordAdapter(restoreList)
            recyclerView.adapter = adapter
            Log.d(TAG, " recyclerView restored. Adapter item count: "+ adapter.itemCount)
//            Log.d(TAG, " recyclerView last word: "+ recyclerView?.getChildAt(adapter.itemCount-1)?.word?.text.toString() )

            // Restore sort bar
            list.let {
                if(!list.isEmpty()) enableSortBar()     // Enable sort bar

                // Sort thumb color, if user sorted
                sort = savedInstanceState.getString("sort")
                sort?.let {
                    Log.d(TAG," restoring sort setting: $sort")
                    when(sort) {        // TODO: use an enumerated class instead of strings
                        Sorting.THUMBS_UP.toString(), "thumbsUp" -> {
                            sort_thumbs_up?.setColorFilter(ContextCompat.getColor(this,R.color.colorAccent))
                            sort_thumbs_down?.setColorFilter(ContextCompat.getColor(this,R.color.thumb))
                        }
                        Sorting.THUMBS_DOWN.toString(), "thumbsDown" -> {
                            sort_thumbs_down?.setColorFilter(ContextCompat.getColor(this,R.color.colorAccent))
                            sort_thumbs_up?.setColorFilter(ContextCompat.getColor(this,R.color.thumb))
                        }
                        else -> ""
                    }

                }
            }
        }

    }

    // enable sort bar if list has data
    private fun enableSortBar(){
        list.let{
            if(!list.isEmpty()){
                Log.d(TAG," Enabling sorting feature.")

                // make sort bar visible
                val sortBar = this.sortBar
                sortBar.visibility = View.VISIBLE

                // Sorting - thumbs up
                val thumbsUp = findViewById<ImageView?>(R.id.sort_thumbs_up)
                val thumbsDown = findViewById<ImageView?>(R.id.sort_thumbs_down)
                thumbsUp?.setOnClickListener{
                    list.sortByDescending { it.thumbsUp }
                    recyclerView.adapter = WordAdapter(list)
                    thumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
                    thumbsDown?.setColorFilter(ContextCompat.getColor(this, R.color.thumb))
                    sort = "thumbsUp"
                    Log.d(TAG, " Sort by thumbs up")
                }

                // Sorting - thumbs down
                thumbsDown?.setOnClickListener{
                    list.sortByDescending{ it.thumbsDown }
                    recyclerView.adapter = WordAdapter(list)
                    thumbsDown.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
                    thumbsUp?.setColorFilter(ContextCompat.getColor(this, R.color.thumb))
                    sort = "thumbsDown"
                    Log.d(TAG, " Sort by thumbs down")
                }
            }
        }
    }

    // ImageView tinting extension function for compatibility
    fun ImageView.setTint(colorId: Int){
        val color = ContextCompat.getColor(applicationContext, colorId)
        val colorStateList = ColorStateList.valueOf(color)
//        val ImageViewCompat.setImageTintList(this, colorStateList)
    }

    private fun mockData(){


        progress_bar.visibility = View.VISIBLE
        progress_bar.isShown

        // populate adapter with mock data for testing purposes

        if(searchTerm.text.toString().isBlank()) {
            list.add(
                WordItem(
                    "Enter a search term",
                    "You've left the search blank when you tapped on the button, so a search could not be done. To use, enter search term, then hit button.",
                    0,
                    0
                )
            )

            list.add(
                WordItem(
                    "Sorting",
                    "After search results come back, you can sort them in order by either thumbs up or thumbs down. This can be done by tapping on the thumb-up or thumb-down icons that appear. Try it now!",
                    1,
                    0
                )
            )

            list.add(
                WordItem(
                    "Sample search terms",
                    "LOL, Clock Method, IMHO, Yuzer, gis, Pizza Pop, 49-1, Amazheimer's",
                    2,
                    0
                )
            )
        }




        if(mockData) {
            list.add(WordItem(searchTerm.text.toString(), "The search term you just put in.", 0, 0))
            list.add(WordItem("aaa", "car club", 30, 40))
            list.add(WordItem("bbb", "better business bureau", 12, 2))
            list.add(WordItem("aa", "alcoholics anonymous", 50, 60))
        }

        list.add(
            WordItem(
                "Clock Method",
                "A method of guessing on a multiple choice test that involves looking at the position of the second hand. If the hand is between 12 and 3 the guess is A. If the hand is between 3 and 6 the guess is B. Between 6 and 9 guess C. Between 9 and 12 guess D.",
                10,
                99
            )
        )

        progress_bar.visibility = View.GONE

        recyclerView.adapter = WordAdapter(list)    // update recyclerView

        Log.d(TAG, "mockData(): Adapter count: " + WordAdapter(list).itemCount)

        // Reset sort thumbs color
        sort_thumbs_up.setColorFilter(ContextCompat.getColor(this, R.color.thumb))
        sort_thumbs_down.setColorFilter(ContextCompat.getColor(this, R.color.thumb))
    }
}
