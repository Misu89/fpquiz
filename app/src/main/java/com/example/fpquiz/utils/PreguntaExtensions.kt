package com.example.fpquiz.utils

import com.example.fpquiz.data.model.Pregunta
import com.example.fpquiz.data.model.RespostaUsuari

fun List<Pregunta>.filtrarPerCategoria(categoria: String?): List<Pregunta> =
    if (categoria == null) this
    else filter { it.categoria == categoria }

fun List<Pregunta>.perCategoria(): Map<String, List<Pregunta>> =
    groupBy { it.categoria }

fun List<RespostaUsuari>.calcularEstadistiques(): Pair<Int, Int> {
    val (correctes, incorrectes) = partition { it.opcioTriada.esCorrecta }
    return Pair(correctes.size, incorrectes.size)
}

fun List<Pregunta>.preguntesAleatories(n: Int): List<Pregunta> =
    asSequence().shuffled().take(n).toList()