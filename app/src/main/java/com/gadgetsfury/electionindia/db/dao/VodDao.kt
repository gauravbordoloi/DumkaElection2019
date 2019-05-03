package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.sveep.vod.VOD

@Dao
interface VodDao {

    @Query("SELECT * FROM VOD ORDER BY id DESC")
    fun getAll(): List<VOD>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<VOD>)

    @Query("DELETE FROM VOD")
    fun clearAll()

}