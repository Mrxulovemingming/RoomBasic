package com.example.roombasic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var buttonInsert: Button
    lateinit var buttonDelete: Button
    lateinit var buttonUpdate: Button
    lateinit var buttonQuery: Button
    lateinit var allWords: LiveData<List<Word>>
    lateinit var wordViewModel: WordViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var wordAdapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wordViewModel = ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this)
        )[WordViewModel::class.java]
        buttonInsert = findViewById(R.id.buttonInsert)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonQuery = findViewById(R.id.buttonQuery)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        recyclerView = findViewById(R.id.recycleview)
        wordAdapter = WordAdapter(wordViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = wordAdapter
        allWords = wordViewModel.getAllWords()
        allWords.observe(this) { // 通过 LiveData 的观察者模式来实现视图的及时更新
            val temp = wordAdapter.itemCount
            wordAdapter.setAllWords(it)
            if (temp != it.size)
                wordAdapter.notifyDataSetChanged()
        }
        buttonInsert.setOnClickListener {
            val word = Word("hello", "你好")
            val word1 = Word("ikun", "小黑子")
            wordViewModel.addWord(word, word1)
        }
        buttonQuery.setOnClickListener {
            wordViewModel.clearWords()
        }
        buttonUpdate.setOnClickListener {
            val word = Word("只因", "鸡")
            word.id = 96
            wordViewModel.updateWord(word)
        }
    }


}