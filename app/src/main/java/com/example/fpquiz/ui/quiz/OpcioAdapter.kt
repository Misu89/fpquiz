package com.example.fpquiz.ui.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fpquiz.R
import com.example.fpquiz.data.model.Opcio
import com.example.fpquiz.databinding.ItemOpcioBinding

class OpcioAdapter(
    private val onOpcioSeleccionada: (Opcio) -> Unit
) : ListAdapter<Opcio, OpcioAdapter.OpcioVH>(OpcioDiff()) {

    var respostaDonada: Opcio? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpcioVH {
        val binding = ItemOpcioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OpcioVH(binding)
    }

    override fun onBindViewHolder(holder: OpcioVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OpcioVH(private val binding: ItemOpcioBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(opcio: Opcio) = with(binding) {
            txtOpcio.text = opcio.text

            val color = when {
                respostaDonada == null -> R.color.neutral
                opcio.esCorrecta -> R.color.green
                opcio == respostaDonada -> R.color.red
                else -> R.color.neutral
            }

            root.setBackgroundResource(color)
            root.setOnClickListener {
                if (respostaDonada == null) onOpcioSeleccionada(opcio)
            }
        }
    }
}