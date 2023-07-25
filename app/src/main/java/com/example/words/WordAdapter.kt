package com.example.words

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(private val wordViewModel: WordViewModel) :
    ListAdapter<Word, WordAdapter.WordViewHolder>(WordDiffCallback()) {
    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewNumber: TextView = view.findViewById(R.id.textViewNumber)
        var textViewEnglish: TextView = view.findViewById(R.id.textViewEnglish)
        var textViewChinese: TextView = view.findViewById(R.id.textViewChinese)
        var switch: Switch = view.findViewById(R.id.showChinese)
    }

    class WordDiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_card2, parent, false)
        val holder = WordViewHolder(view)
        holder.itemView.setOnClickListener { // 将点击事件放在 onCreate 中，如果放在 onBind 中每次都会刷新，添加新的点击事件
            val uri =
                Uri.parse("https://dict.youdao.com/result?word=${holder.textViewEnglish.text}&lang=en")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            val word: Word = holder.itemView.getTag(R.id.word_for_view_holder) as Word
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
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = getItem(position)
        holder.itemView.setTag(R.id.word_for_view_holder, word)// tag 可以放任何对象
        holder.textViewNumber.text = (position + 1).toString()
        holder.textViewEnglish.text = word.word
        holder.textViewChinese.text = word.chineseMeaning
        if (word.showChinese) {
            holder.textViewChinese.visibility = View.VISIBLE
            holder.switch.isChecked = true
        } else {
            holder.textViewChinese.visibility = View.GONE
            holder.switch.isChecked = false
        }
    }

    // <editor-fold defaultstate="collapsed" desc="防止滑动到窗口外的控件到窗口内时，标签未更新">
    override fun onViewAttachedToWindow(holder: WordViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.textViewNumber.text = (holder.adapterPosition + 1).toString()
    }
    // </editor-fold>
    


}