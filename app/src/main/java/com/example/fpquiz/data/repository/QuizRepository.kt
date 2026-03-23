package com.example.fpquiz.data.repository

import android.content.Context
import com.example.fpquiz.data.local.PartidaDao
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.data.model.Pregunta
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class QuizRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val partidaDao: PartidaDao
) {
    suspend fun carregarPreguntes(): List<Pregunta> =
        withContext(Dispatchers.IO) {
            val json = context.assets
                .open("preguntes.json")
                .bufferedReader()
                .use { it.readText() }

            Json.decodeFromString<List<Pregunta>>(json)
        }

    fun observarPartides(): Flow<List<PartidaEntity>> =
        partidaDao.observarTotes()

    suspend fun guardarPartida(partida: PartidaEntity) =
        partidaDao.inserir(partida)
}