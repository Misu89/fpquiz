package com.example.fpquiz.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import android.widget.Toast
import com.example.fpquiz.R
import com.example.fpquiz.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import androidx.core.content.ContextCompat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val viewModel: QuizViewModel by viewModels()
    private lateinit var binding: FragmentQuizBinding

    private val opcioAdapter = OpcioAdapter { opcio ->
        viewModel.respondre(opcio)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizBinding.bind(view)

        binding.recyclerOpcions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerOpcions.adapter = opcioAdapter
        binding.btnSeguent.setOnClickListener {
            viewModel.seguent()
        }

        val categoria = arguments?.getString("categoria")
        viewModel.iniciarQuiz(categoria)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { estat ->
                    renderEstat(estat)
                }
            }
        }
    }

    private fun renderEstat(estat: QuizUiState) = with(binding) {
        when (estat) {
            is QuizUiState.Carregant -> {
                progressBar.isVisible = true
                layoutPregunta.isVisible = false
            }

            is QuizUiState.PreguntaActiva -> {
                progressBar.isVisible = false
                layoutPregunta.isVisible = true
                txtPregunta.text = estat.pregunta.text
                txtProgres.text = "${estat.indexActual} / ${estat.total}"

                progressTimer.max = QuizUiState.PreguntaActiva.TEMPS_INICIAL
                progressTimer.progress = estat.segonsRestants
                txtTimer.text = "${estat.segonsRestants}s"

                val colorTimer = when {
                    estat.segonsRestants > 10 -> R.color.green
                    estat.segonsRestants > 5 -> R.color.neutral
                    else -> R.color.red
                }
                txtTimer.setTextColor(ContextCompat.getColor(requireContext(), colorTimer))

                opcioAdapter.submitList(estat.pregunta.opcions)
                opcioAdapter.respostaDonada = estat.respostaDonada
                btnSeguent.isEnabled = estat.respostaDonada != null
            }

            is QuizUiState.Finalitzat -> {
                val jsonIncorrectes = Json.encodeToString(estat.respostes)

                findNavController().currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("respostesFinals", jsonIncorrectes)

                val args = bundleOf(
                    "puntuacio" to estat.puntuacio,
                    "total" to estat.total
                )

                findNavController().navigate(
                    R.id.action_quizFragment_to_resultsFragment,
                    args
                )
            }

            is QuizUiState.Error -> {
                Toast.makeText(requireContext(), estat.missatge, Toast.LENGTH_LONG).show()
            }
        }
    }
}
