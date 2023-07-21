package com.example.roombasic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var allWords: List<Word> = ArrayList()
    fun setAllWords(words: List<Word>) {
        allWords = words
    }

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewNumber: TextView = view.findViewById(R.id.textViewNumber)
        var textViewEnglish: TextView = view.findViewById(R.id.textViewChinese)
        var textViewChinese: TextView = view.findViewById(R.id.textViewEnglish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_normal, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = allWords[position]
        holder.textViewNumber.text = (position + 1).toString()
        holder.textViewEnglish.text = word.word
        holder.textViewChinese.text = word.chineseMeaning
    }

    override fun getItemCount(): Int {
        return allWords.size
    }
}