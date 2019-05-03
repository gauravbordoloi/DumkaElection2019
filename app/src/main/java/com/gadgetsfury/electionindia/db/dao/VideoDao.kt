package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.sveep.video.Video

@Dao
interface VideoDao {

    @Query("SELECT * FROM Video")
    fun getAll(): List<Video>

    @Query("SELECT * FROM Video WHERE playlistId = :playlistId")
    fun getAllByPlaylistId(playlistId: String): List<Video>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Video>)

    @Query("DELETE FROM Video WHERE playlistId = :playlistId")
    fun clearAll(playlistId: String)

}