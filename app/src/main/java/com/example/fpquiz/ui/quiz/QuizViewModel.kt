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
                        .filter { categoria == null || it.categoria == categoria }
                        .shuffled()
                        .take(10)
                    mostrarSeguent()
                }
                .onFailure {
                    _uiState.value = QuizUiState.Error(it.message ?: "Error desconegut")
                }
        }
    }

    private fun mostrarSeguent() {
        _uiState.value = QuizUiState.PreguntaActiva(
            pregunta = preguntes[indexActual],
            indexActual = indexActual + 1,
            total = preguntes.size
        )
    }
}