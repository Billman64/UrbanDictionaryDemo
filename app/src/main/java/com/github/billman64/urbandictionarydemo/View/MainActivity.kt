/* Urban Dictionary API call demo for Nike
    @author: Bill Lugo

    Copyrights:
        Background photo by Devon Delrio is licensed under Creative Commons CC0 (Public Domain). Source: https://pixy.org/107734/
 */

package com.github.billman64.urbandictionarydemo.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.billman64.urbandictionarydemo.Model.WordAdapter
import com.github.billman64.urbandictionarydemo.Model.WordItem
import com.github.billman64.urbandictionarydemo.R

class MainActivity : AppCompatActivity() {
    val TAG:String="Nike demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate()")

        //TODO: load cached data on orientation change


        var recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val button:Button = findViewById(R.id.searchButton)

        button.setOnClickListener{
            Toast.makeText(this,"network API call to be implemented", Toast.LENGTH_SHORT).show()
        }


        // temporary mock data
        var list = ArrayList<WordItem>()
        list.add(
            WordItem(
                "Clock Method",
                "A method of guessing on a multiple choice test that involves looking at the position of the second hand. If the hand is between 12 and 3 the guess is A. If the hand is between 3 and 6 the guess is B. Between 6 and 9 guess C. Between 9 and 12 guess D.",
                10,
                20
            )
        )
        list.add(WordItem("aaa", "car club", 30, 40))
        list.add(
            WordItem(
                "aa",
                "alcoholics anonymous",
                50,
                60
            )
        )


        // populate recyclerView, using a custom adapter
        var adapter = WordAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }
}
