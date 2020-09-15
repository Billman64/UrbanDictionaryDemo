package com.github.billman64.urbandictionarydemo.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.billman64.urbandictionarydemo.R
import kotlinx.android.synthetic.main.word_item.view.*

class WordAdapter(private val wordList: ArrayList<WordItem>): RecyclerView.Adapter<WordAdapter.WordViewHolder>(){

    class WordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){        // ViewHolder sub-class
        // View references of an individual item
        val wordView:TextView = itemView.word
        val definitionView:TextView = itemView.definition
        val thumbsUpView:TextView = itemView.thumbsUpCount
        val thumbsDownView:TextView = itemView.thumbsDownCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {     // standard layout inflation
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {      // views + data loaded as recyclerView scrolls
        val currentItem = wordList[position]

        holder.wordView.text = currentItem.word
        holder.definitionView.text = currentItem.definition
        holder.thumbsUpView.text = currentItem.thumbsUp.toString()
        holder.thumbsDownView.text = currentItem.thumbsDown.toString()

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.fade_translate) // custom, minimal animation

        // The less is done here, the better the performance, since onBindViewHolder is called frequently.
    }

    override fun getItemCount() = wordList.size     // get number of items in collection
}