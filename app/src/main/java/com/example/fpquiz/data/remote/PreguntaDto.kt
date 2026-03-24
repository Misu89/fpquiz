package com.example.fpquiz.data.remote

import com.example.fpquiz.data.model.Dificultat
import com.example.fpquiz.data.model.Opcio
import com.example.fpquiz.data.model.Pregunta
import kotlinx.serialization.Serializable

@Serializable
data class PreguntaDto(
    val id: Int,
    val text: String,
    val opcions: List<OpcioDto>,
    val explicacio: String? = null,
    val categoria: String = "Remota",
    val dificultat: String = "MITJA"
)

@Serializable
data class OpcioDto(
    val text: String,
    val esCorrecta: Boolean
)

fun PreguntaDto.toPregunta(): Pregunta = Pregunta(
    id = id,
    text = text,
    opcions = opcions.map { Opcio(text = it.text, esCorrecta = it.esCorrecta) },
    explicacio = explicacio,
    categoria = categoria,
    dificultat = runCatching {
        Dificultat.valueOf(dificultat)
    }.getOrDefault(Dificultat.MITJA)
)