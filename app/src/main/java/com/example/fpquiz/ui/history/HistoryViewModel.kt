package com.example.fpquiz.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> =
        repository.observarPartides()
            .map { partides -> calcularEstadistiques(partides) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HistoryUiState()
            )

    private fun calcularEstadistiques(partides: List<PartidaEntity>): HistoryUiState {
        if (partides.isEmpty()) return HistoryUiState()

        val percentatges = partides.map { partida ->
            partida.puntuacio.toFloat() / partida.totalPreguntes * 100
        }

        return HistoryUiState(
            partides = partides,
            nombrePartides = partides.size,
            millorPuntuacio = percentatges.maxOrNull() ?: 0f,
            pitjorPuntuacio = percentatges.minOrNull() ?: 0f,
            mitjana = percentatges.average().toFloat()
        )
    }
}

data class HistoryUiState(
    val partides: List<PartidaEntity> = emptyList(),
    val nombrePartides: Int = 0,
    val millorPuntuacio: Float = 0f,
    val pitjorPuntuacio: Float = 0f,
    val mitjana: Float = 0f
)