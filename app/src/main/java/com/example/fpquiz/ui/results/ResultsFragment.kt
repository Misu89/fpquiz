package com.example.fpquiz.ui.results

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fpquiz.R
import com.example.fpquiz.data.model.RespostaUsuari
import com.example.fpquiz.databinding.FragmentResultsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ResultsFragment : Fragment(R.layout.fragment_results) {

    private lateinit var binding: FragmentResultsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultsBinding.bind(view)

        val puntuacio = arguments?.getInt("puntuacio") ?: 0
        val total = arguments?.getInt("total") ?: 0

        binding.txtResultat.text = "Puntuació: $puntuacio / $total"

        val jsonRespostes = findNavController().previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("respostesFinals")

        val respostesFinals = jsonRespostes
            ?.let { Json.decodeFromString<List<RespostaUsuari>>(it) }
            .orEmpty()

        val incorrectes = respostesFinals.filter { !it.opcioTriada.esCorrecta }

        binding.btnRevisio.isVisible = incorrectes.isNotEmpty()

        binding.btnRevisio.setOnClickListener {
            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.set("incorrectes", Json.encodeToString(incorrectes))

            findNavController().navigate(R.id.reviewFragment)
        }

        binding.btnTornarInici.setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }
    }
}