package com.example.roombasic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WordViewModel(application: Application) :
    AndroidViewModel(application) {
    lateinit var wordDao: WordDao
    lateinit var allWords: LiveData<List<Word>>

    init {
        wordDao = WordDatabase.getWordDataBase(application).getWordDao()
        allWords = wordDao.getAllWords()
    }

    fun addWord(vararg word: Word) {
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.insertWords(*word)
        }
    }

    fun clearWords(){
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.deleteAllWords()
        }
    }

    fun updateWord(word: Word){
        CoroutineScope(Dispatchers.IO).launch {
            wordDao.updateWords(word)
        }
    }

}