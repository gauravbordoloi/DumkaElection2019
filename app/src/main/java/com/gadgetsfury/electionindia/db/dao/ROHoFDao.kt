package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.sveep.rohof.ROHoF

@Dao
interface ROHoFDao {

    @Query("SELECT * FROM ROHoF ORDER BY id DESC")
    fun getAll(): List<ROHoF>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<ROHoF>)

    @Query("DELETE FROM ROHoF")
    fun clearAll()

}