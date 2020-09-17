/* Urban Dictionary API call demo for Nike
    @author: Bill Lugo

    Copyrights:
        Data source: Urban Dictionary (UrbanDictionary.com)
        Background photo by Devon Delrio is licensed under Creative Commons CC0 (Public Domain). Source: https://pixy.org/107734/
 */

package com.github.billman64.urbandictionarydemo.View

import android.content.res.ColorStateList
import android.graphics.ColorSpace
import android.opengl.Visibility
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.billman64.urbandictionarydemo.Model.WordAdapter
import com.github.billman64.urbandictionarydemo.Model.WordItem
import com.github.billman64.urbandictionarydemo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.word_item.*
import kotlinx.android.synthetic.main.word_item.view.*

class MainActivity : AppCompatActivity() {
    val TAG:String="Nike demo"
    private var list = ArrayList<WordItem>()
    private val mockData = true
    private var sort:String?=""

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
            Toast.makeText(this,"network API call to be implemented", Toast.LENGTH_SHORT).show()

            // reset list data
            list.clear()

            // API call or mock data
            if(!mockData) {
                // API network call

            } else {

                // populate adapter with mock data for testing purposes
                list.add(
                    WordItem(
                        "Clock Method",
                        "A method of guessing on a multiple choice test that involves looking at the position of the second hand. If the hand is between 12 and 3 the guess is A. If the hand is between 3 and 6 the guess is B. Between 6 and 9 guess C. Between 9 and 12 guess D.",
                        10,
                        20
                    )
                )

                list.add(WordItem("aaa", "car club", 30, 40))
                list.add(WordItem("bbb", "better business bureau", 12, 2))
                list.add(WordItem("aa", "alcoholics anonymous", 50, 60))

                recyclerView.adapter = WordAdapter(list)    // update recyclerView

                Log.d(TAG, "Button pressed. adapter count: " + WordAdapter(list).itemCount)
            }

            list.let { if(!list.isEmpty()) enableSortBar() }   // enable sort bar if list has data
        }
    }

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
                        "thumbsUp" -> {
                            sort_thumbs_up?.setColorFilter(ContextCompat.getColor(this,R.color.colorAccent))
                            sort_thumbs_down?.setColorFilter(ContextCompat.getColor(this,R.color.thumb))
                        }
                        "thumbsDown" -> {
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
                var sortBar = this.sortBar
                sortBar.visibility = View.VISIBLE

                // Sorting - thumbs up
                val thumbsUp = findViewById<ImageView?>(R.id.sort_thumbs_up)
                val thumbsDown = findViewById<ImageView?>(R.id.sort_thumbs_down)
                thumbsUp?.setOnClickListener{
                    list.sortBy { it.thumbsUp }
                    recyclerView.adapter = WordAdapter(list)
                    thumbsUp.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
                    thumbsDown?.setColorFilter(ContextCompat.getColor(this, R.color.thumb))
                    sort = "thumbsUp"
                    Log.d(TAG, " Sort by thumbs up")
                }

                // Sorting - thumbs down
                thumbsDown?.setOnClickListener{
                    list.sortBy { it.thumbsDown }
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
}
