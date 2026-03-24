package com.example.fpquiz.ui.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fpquiz.R
import com.example.fpquiz.data.model.RespostaUsuari
import com.example.fpquiz.databinding.ItemReviewBinding

class ReviewAdapter
    : ListAdapter<RespostaUsuari, ReviewAdapter.ReviewVH>(ReviewDiff()) {

    inner class ReviewVH(private val binding: ItemReviewBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(resposta: RespostaUsuari) = with(binding) {
            val pregunta = resposta.pregunta

            txtPregunta.text = pregunta.text
            txtRespostaDonada.text = "La teva: ${resposta.opcioTriada.text}"
            txtRespostaDonada.setTextColor(
                ContextCompat.getColor(root.context, R.color.red)
            )

            val correcta = pregunta.opcions.firstOrNull { it.esCorrecta }
            txtRespostaCorrecta.text = "Correcta: ${correcta?.text ?: "?"}"
            txtRespostaCorrecta.setTextColor(
                ContextCompat.getColor(root.context, R.color.green)
            )

            pregunta.explicacio?.let { explicacio ->
                txtExplicacio.isVisible = true
                txtExplicacio.text = "Explicació: $explicacio"
            } ?: run {
                txtExplicacio.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewVH(ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: ReviewVH, position: Int) =
        holder.bind(getItem(position))
}

class ReviewDiff : DiffUtil.ItemCallback<RespostaUsuari>() {
    override fun areItemsTheSame(old: RespostaUsuari, new: RespostaUsuari) =
        old.pregunta.id == new.pregunta.id

    override fun areContentsTheSame(old: RespostaUsuari, new: RespostaUsuari) =
        old == new
}