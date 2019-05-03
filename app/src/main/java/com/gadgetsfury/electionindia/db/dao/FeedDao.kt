package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.feeds.Feed

@Dao
interface FeedDao {

    @Query("SELECT * FROM Feed ORDER BY id DESC")
    fun getAll(): List<Feed>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Feed>)

    @Query("DELETE FROM Feed")
    fun clearAll()

}