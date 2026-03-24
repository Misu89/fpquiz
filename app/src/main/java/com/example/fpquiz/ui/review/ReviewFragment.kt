package com.example.fpquiz.ui.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fpquiz.R
import com.example.fpquiz.data.model.RespostaUsuari
import com.example.fpquiz.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class ReviewFragment : Fragment(R.layout.fragment_review) {

    private lateinit var binding: FragmentReviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewBinding.bind(view)

        val adapter = ReviewAdapter()
        binding.recyclerReview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReview.adapter = adapter

        val jsonIncorrectes = findNavController().previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("incorrectes")

        val incorrectes = jsonIncorrectes
            ?.let { Json.decodeFromString<List<RespostaUsuari>>(it) }
            .orEmpty()

        binding.txtResum.text = "${incorrectes.size} respostes incorrectes"
        adapter.submitList(incorrectes)
    }
}