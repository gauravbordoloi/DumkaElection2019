package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.sveep.vfeed.VoterFeed

@Dao
interface VoterFeedDao {

    @Query("SELECT * FROM VoterFeed ORDER BY id DESC")
    fun getAll(): List<VoterFeed>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<VoterFeed>)

    @Query("DELETE FROM VoterFeed")
    fun clearAll()

}