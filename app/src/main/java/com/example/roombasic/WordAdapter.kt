package com.example.roombasic

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(val wordViewModel: WordViewModel) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var allWords: List<Word> = ArrayList()
    fun setAllWords(words: List<Word>) {
        allWords = words
    }

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewNumber: TextView = view.findViewById(R.id.textViewNumber)
        var textViewEnglish: TextView = view.findViewById(R.id.textViewChinese)
        var textViewChinese: TextView = view.findViewById(R.id.textViewEnglish)
        var switch: Switch = view.findViewById(R.id.showChinese)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_card2, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = allWords[position]
        holder.textViewNumber.text = (position + 1).toString()
        holder.textViewEnglish.text = word.word
        holder.textViewChinese.text = word.chineseMeaning
        holder.switch.setOnCheckedChangeListener(null) // 这句话很关键
        if (word.showChinese) {
            holder.textViewChinese.visibility = View.GONE
            holder.switch.isChecked = true
        } else {
            holder.textViewChinese.visibility = View.VISIBLE
            holder.switch.isChecked = false
        }
        holder.itemView.setOnClickListener {
            val uri = Uri.parse("https://dict.youdao.com/result?word=good&lang=en")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                holder.textViewChinese.visibility = View.VISIBLE
                word.showChinese = true
                wordViewModel.updateWord(word)

            } else {
                holder.textViewChinese.visibility = View.GONE
                word.showChinese = false
                wordViewModel.updateWord(word)
            }

        }
    }

    override fun getItemCount(): Int {
        return allWords.size
    }
}