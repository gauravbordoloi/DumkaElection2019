package com.gadgetsfury.electionindia.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.gadgetsfury.electionindia.candidate.Candidate

@Dao
interface CandidateDao {

    @Query("SELECT * FROM Candidate ORDER BY name ASC")
    fun getAll(): List<Candidate>

    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<Candidate>)

    @Query("DELETE FROM Candidate")
    fun clearAll()

}