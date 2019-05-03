package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.contacts.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact ORDER BY priority ASC")
    fun getAll(): List<Contact>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Contact>)

    @Query("DELETE FROM Contact")
    fun clearAll()

}