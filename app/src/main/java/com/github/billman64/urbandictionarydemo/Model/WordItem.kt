package com.github.billman64.urbandictionarydemo.Model

// word poko

data class WordItem(val word:String, val definition:String, val thumbsUp:Int, val thumbsDown:Int){
    var mWord:String
        internal set
    var mDefinition:String
        internal set
    var mThumbsUp:Int
        internal set
    var mThumbsDown:Int
        internal set

    init{
        mWord = word
        mDefinition = definition
        mThumbsUp = thumbsUp
        mThumbsDown = thumbsDown
    }






}
