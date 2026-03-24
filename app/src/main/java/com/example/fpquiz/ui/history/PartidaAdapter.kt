package com.example.fpquiz.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fpquiz.R
import com.example.fpquiz.data.local.PartidaEntity
import com.example.fpquiz.databinding.ItemPartidaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PartidaAdapter : ListAdapter<PartidaEntity, PartidaAdapter.PartidaVH>(PartidaDiff()) {

    inner class PartidaVH(private val binding: ItemPartidaBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(partida: PartidaEntity) = with(binding) {
            val pct = partida.puntuacio.toFloat() / partida.totalPreguntes * 100

            txtPuntuacio.text = "${partida.puntuacio} / ${partida.totalPreguntes}"
            txtPercentatge.text = "%.1f%%".format(pct)
            txtData.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(Date(partida.data))

            val color = when {
                pct >= 80f -> R.color.green
                pct >= 50f -> R.color.neutral
                else -> R.color.red
            }
            txtPercentatge.setTextColor(ContextCompat.getColor(root.context, color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PartidaVH(ItemPartidaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: PartidaVH, position: Int) =
        holder.bind(getItem(position))
}

class PartidaDiff : DiffUtil.ItemCallback<PartidaEntity>() {
    override fun areItemsTheSame(old: PartidaEntity, new: PartidaEntity) = old.id == new.id
    override fun areContentsTheSame(old: PartidaEntity, new: PartidaEntity) = old == new
}