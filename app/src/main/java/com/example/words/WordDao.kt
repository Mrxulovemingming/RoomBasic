package com.example.words

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Insert
    fun insertWords(vararg word: Word) // 可变长度的参数

    @Update
    fun updateWords(vararg word: Word): Int

    @Delete
    fun deleteWords(vararg word: Word): Int

    @Query("DELETE FROM WORD")
    fun deleteAllWords()

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM WORD WHERE word LIKE  :key  ORDER BY ID DESC ")
    fun getSearchWords(key:String): LiveData<List<Word>>
}