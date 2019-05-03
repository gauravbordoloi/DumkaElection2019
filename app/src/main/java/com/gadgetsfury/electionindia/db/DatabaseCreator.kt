package com.gadgetsfury.electionindia.db

import android.content.Context
import androidx.room.Room



class DatabaseCreator {

    private var appDatabase: AppDatabase? = null
    private val LOCK = Any()

    @Synchronized
    fun getAppDatabase(context: Context): AppDatabase {
        if (appDatabase == null) {
            synchronized(LOCK) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "com.gadgetsfury.electionindia"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration().build()
                }
            }
        }
        return appDatabase!!
    }

    fun clearAllTables() {
        if (appDatabase != null) {
            appDatabase!!.clearAllTables()
        }
    }

}