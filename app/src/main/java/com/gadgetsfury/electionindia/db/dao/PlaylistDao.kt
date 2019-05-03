package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.sveep.video.Playlist

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM Playlist")
    fun getAll(): List<Playlist>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Playlist>)

    @Query("DELETE FROM Playlist")
    fun clearAll()

}