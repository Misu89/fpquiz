package com.example.fpquiz.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.data.model.Opcio
import com.example.fpquiz.data.model.Pregunta
import com.example.fpquiz.data.model.RespostaUsuari
import com.example.fpquiz.data.repository.QuizRepository
import com.example.fpquiz.utils.filtrarPerCategoria
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
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
    private var timerJob: Job? = null

    fun iniciarQuiz(categoria: String? = null) {
        timerJob?.cancel()
        indexActual = 0
        respostes.clear()

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
        timerJob?.cancel()

        val estat = _uiState.value as? QuizUiState.PreguntaActiva ?: return
        respostes.add(RespostaUsuari(estat.pregunta, opcio))
        _uiState.value = estat.copy(respostaDonada = opcio)
    }

    private fun mostrarSeguent() {
        _uiState.value = QuizUiState.PreguntaActiva(
            pregunta = preguntes[indexActual],
            indexActual = indexActual + 1,
            total = preguntes.size,
            segonsRestants = QuizUiState.PreguntaActiva.TEMPS_INICIAL
        )
        iniciarTimer()
    }

    fun seguent() {
        indexActual++
        if (indexActual < preguntes.size) mostrarSeguent()
        else finalitzar()
    }

    private fun finalitzar() {
        timerJob?.cancel()

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

    private fun iniciarTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            flow {
                repeat(QuizUiState.PreguntaActiva.TEMPS_INICIAL) { i ->
                    emit(QuizUiState.PreguntaActiva.TEMPS_INICIAL - i)
                    delay(1000)
                }
                emit(0)
            }.collect { segons ->
                val estat = _uiState.value as? QuizUiState.PreguntaActiva ?: return@collect
                _uiState.value = estat.copy(segonsRestants = segons)

                if (segons == 0 && estat.respostaDonada == null) {
                    tempsEsgotat()
                }
            }
        }
    }

    private fun tempsEsgotat() {
        val estat = _uiState.value as? QuizUiState.PreguntaActiva ?: return

        val respostaIncorrecta = estat.pregunta.opcions.first { !it.esCorrecta }
        respostes.add(RespostaUsuari(estat.pregunta, respostaIncorrecta))
        seguent()
    }
}