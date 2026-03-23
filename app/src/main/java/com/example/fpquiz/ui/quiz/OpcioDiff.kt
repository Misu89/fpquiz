package com.example.fpquiz.ui.quiz

import androidx.recyclerview.widget.DiffUtil
import com.example.fpquiz.data.model.Opcio

class OpcioDiff : DiffUtil.ItemCallback<Opcio>() {
    override fun areItemsTheSame(oldItem: Opcio, newItem: Opcio): Boolean =
        oldItem.text == newItem.text

    override fun areContentsTheSame(oldItem: Opcio, newItem: Opcio): Boolean =
        oldItem == newItem
}