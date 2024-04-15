package com.avv.restauranteordenes.ui

import androidx.recyclerview.widget.RecyclerView
import com.avv.restauranteordenes.R
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.databinding.RestauranteElementBinding


class RestauranteViewHolder(private val binding: RestauranteElementBinding): RecyclerView.ViewHolder(binding.root) {
    val ivIcon = binding.ivIcon

    fun bind(restaurante : RestauranteEntity){
        binding.apply {
            tvPlayer.text = restaurante.comida
            tvTeam.text = restaurante.nombre
            tvNationality.text = restaurante.propina


            ivIcon.setImageResource(getRestauranteImageResource(restaurante.comida))
        }
    }

    private fun getRestauranteImageResource(OrdenComida: String): Int {
        return when (OrdenComida) {
            "Chilaquiles" -> R.drawable.chilaquiles
            "Tamales" -> R.drawable.tamales
            "Quesadillas" -> R.drawable.quesadillas
            else -> R.drawable.quesadillas //
        }
    }
}