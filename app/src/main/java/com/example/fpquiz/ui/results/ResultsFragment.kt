package com.example.fpquiz.ui.results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fpquiz.R
import com.example.fpquiz.databinding.FragmentResultsBinding

class ResultsFragment : Fragment(R.layout.fragment_results) {

    private lateinit var binding: FragmentResultsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultsBinding.bind(view)

        val puntuacio = arguments?.getInt("puntuacio") ?: 0
        val total = arguments?.getInt("total") ?: 0

        binding.txtResultat.text = "Puntuació: $puntuacio / $total"

        binding.btnTornarInici.setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }
    }
}