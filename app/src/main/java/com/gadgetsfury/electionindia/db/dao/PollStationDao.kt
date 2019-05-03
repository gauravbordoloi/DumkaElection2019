package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.poll.PollingStation

@Dao
interface PollStationDao {

    @Query("SELECT * FROM PollingStation ORDER BY id DESC")
    fun getAll(): List<PollingStation>

    @Query("SELECT * FROM PollingStation WHERE assembly = :assembly AND block = :block ORDER BY id DESC")
    fun getByBlock(assembly: String, block: String): List<PollingStation>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<PollingStation>)

    @Query("DELETE FROM PollingStation")
    fun clearAll()

    @Query("DELETE FROM PollingStation WHERE assembly = :assembly AND block = :block")
    fun clearAllByBlock(assembly: String, block: String)

}