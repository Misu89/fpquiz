package com.example.fpquiz.data.repository

import android.content.Context
import com.example.fpquiz.data.local.PartidaDao
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.data.model.Pregunta
import com.example.fpquiz.data.remote.QuizApiService
import com.example.fpquiz.data.remote.toPregunta
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class QuizRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val partidaDao: PartidaDao,
    private val apiService: QuizApiService
) {
    suspend fun carregarPreguntes(): List<Pregunta> =
        withContext(Dispatchers.IO) {
            val locals = carregarPreguntesLocals()

            val remotes = runCatching {
                apiService.getPreguntes().map { it.toPregunta() }
            }.getOrDefault(emptyList())

            locals + remotes
        }

    fun observarPartides(): Flow<List<PartidaEntity>> =
        partidaDao.observarTotes()

    suspend fun guardarPartida(partida: PartidaEntity) =
        partidaDao.inserir(partida)

    private fun carregarPreguntesLocals(): List<Pregunta> {
        val json = context.assets
            .open("preguntes.json")
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString(json)
    }
}