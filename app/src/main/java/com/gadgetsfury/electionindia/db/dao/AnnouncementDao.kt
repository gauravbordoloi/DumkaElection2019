package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.announcement.Announcement

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM Announcement ORDER BY id DESC")
    fun getAll(): List<Announcement>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Announcement>)

    @Query("DELETE FROM Announcement")
    fun clearAll()

}