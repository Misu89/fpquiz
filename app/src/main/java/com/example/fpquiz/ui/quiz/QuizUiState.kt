package com.example.fpquiz.ui.quiz

import com.example.fpquiz.data.model.Opcio
import com.example.fpquiz.data.model.Pregunta
import com.example.fpquiz.data.model.RespostaUsuari

sealed class QuizUiState {
    object Carregant : QuizUiState()

    data class PreguntaActiva(
        val pregunta: Pregunta,
        val indexActual: Int,
        val total: Int,
        val respostaDonada: Opcio? = null
    ) : QuizUiState()

    data class Finalitzat(
        val puntuacio: Int,
        val total: Int,
        val respostes: List<RespostaUsuari>
    ) : QuizUiState()

    data class Error(val missatge: String) : QuizUiState()
}