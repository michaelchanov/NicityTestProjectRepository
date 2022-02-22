package com.nicity.data.datasource.database.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nicity.data.datasource.database.room.dao.NewsDao
import com.nicity.data.datasource.database.room.entities.NewsDB

@Database(entities = [NewsDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao

    companion object {

        private const val DATABASE_NAME = "database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE != null) {
                return INSTANCE as AppDatabase
            }

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()

                return INSTANCE as AppDatabase
            }
        }
    }
}