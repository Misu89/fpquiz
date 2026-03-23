package com.example.fpquiz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDao {
    @Query("SELECT * FROM partides ORDER BY data DESC")
    fun observarTotes(): Flow<List<PartidaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(partida: PartidaEntity): Long

    @Query("SELECT AVG(CAST(puntuacio AS FLOAT) / totalPreguntes * 100) FROM partides")
    suspend fun mitjanaGlobal(): Float?
}