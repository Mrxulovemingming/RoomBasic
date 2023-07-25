package com.example.words

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Word::class], version = 2)
abstract class WordDatabase : RoomDatabase() {
    abstract fun getWordDao(): WordDao

    companion object { // 实现 WordDatabase 的单例模式
        @Volatile
        private var INSTANCE: WordDatabase? = null
        fun getWordDataBase(context: Context): WordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE word ADD COLUMN showChinese INTEGER NOT NULL DEFAULT 1")
            }
        }
    }

}