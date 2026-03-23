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

        viewModel.iniciarQuiz()

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
                opcioAdapter.submitList(estat.pregunta.opcions)
                opcioAdapter.respostaDonada = estat.respostaDonada
                btnSeguent.isEnabled = estat.respostaDonada != null
            }
            is QuizUiState.Finalitzat -> {
                Toast.makeText(requireContext(), "Quiz finalitzat", Toast.LENGTH_SHORT).show()
            }
            is QuizUiState.Error -> {
                Toast.makeText(requireContext(), estat.missatge, Toast.LENGTH_LONG).show()
            }
        }
    }
}
