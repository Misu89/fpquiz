package com.example.fpquiz.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pregunta(
    val id: Int,
    val text: String,
    val opcions: List<Opcio>,
    val explicacio: String? = null,
    val categoria: String = "General",
    val dificultat: Dificultat = Dificultat.MITJA
)

@Serializable
data class Opcio(
    val text: String,
    val esCorrecta: Boolean
)

@Serializable
enum class Dificultat { FACIL, MITJA, DIFICIL }

@Serializable
data class RespostaUsuari(
    val pregunta: Pregunta,
    val opcioTriada: Opcio
)