package com.example.fpquiz.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fpquiz.data.model.Pregunta
import com.example.fpquiz.data.model.RespostaUsuari
import com.example.fpquiz.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.data.model.Opcio
import com.example.fpquiz.utils.filtrarPerCategoria

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Carregant)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var preguntes: List<Pregunta> = emptyList()
    private var indexActual = 0
    private val respostes = mutableListOf<RespostaUsuari>()

    fun iniciarQuiz(categoria: String? = null) {
        viewModelScope.launch {
            runCatching { repository.carregarPreguntes() }
                .onSuccess { totes ->
                    preguntes = totes
                        .filtrarPerCategoria(categoria)
                        .shuffled()
                        .take(10)
                    mostrarSeguent()
                }
                .onFailure {
                    _uiState.value = QuizUiState.Error(it.message ?: "Error desconegut")
                }
        }
    }

    fun respondre(opcio: Opcio) {
        val estat = _uiState.value as? QuizUiState.PreguntaActiva ?: return
        respostes.add(RespostaUsuari(estat.pregunta, opcio))
        _uiState.value = estat.copy(respostaDonada = opcio)
    }

    private fun mostrarSeguent() {
        _uiState.value = QuizUiState.PreguntaActiva(
            pregunta = preguntes[indexActual],
            indexActual = indexActual + 1,
            total = preguntes.size
        )
    }

    fun seguent() {
        indexActual++
        if (indexActual < preguntes.size) mostrarSeguent()
        else finalitzar()
    }

    private fun finalitzar() {
        val correctes = respostes.count { it.opcioTriada.esCorrecta }
        _uiState.value = QuizUiState.Finalitzat(correctes, preguntes.size, respostes)

        viewModelScope.launch {
            repository.guardarPartida(
                PartidaEntity(
                    puntuacio = correctes,
                    totalPreguntes = preguntes.size,
                    categoria = null
                )
            )
        }
    }
}