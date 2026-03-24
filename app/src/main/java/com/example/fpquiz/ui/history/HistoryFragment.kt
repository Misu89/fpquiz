package com.example.fpquiz.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fpquiz.R
import com.example.fpquiz.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: FragmentHistoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        val adapter = PartidaAdapter()
        binding.recyclerPartides.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPartides.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { estat ->
                    adapter.submitList(estat.partides)
                    binding.txtNombrePartides.text = "Partides: ${estat.nombrePartides}"
                    binding.txtMillor.text = "Millor: ${"%.1f".format(estat.millorPuntuacio)}%"
                    binding.txtPitjor.text = "Pitjor: ${"%.1f".format(estat.pitjorPuntuacio)}%"
                    binding.txtMitjana.text = "Mitjana: ${"%.1f".format(estat.mitjana)}%"
                }
            }
        }
    }
}