package com.example.fpquiz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partides")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val data: Long = System.currentTimeMillis(),
    val puntuacio: Int,
    val totalPreguntes: Int,
    val categoria: String?
)