package com.avv.restauranteordenes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.databinding.RestauranteElementBinding


class RestauranteAdapter(private val onPlayerClicked: (RestauranteEntity) -> Unit): RecyclerView.Adapter<RestauranteViewHolder>() {

    private var restaurantes: List<RestauranteEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val binding = RestauranteElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestauranteViewHolder(binding)
    }

    override fun getItemCount(): Int = restaurantes.size


    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {

        val restaurante = restaurantes[position]

        holder.bind(restaurante)



        holder.itemView.setOnClickListener {

            onPlayerClicked(restaurante)
        }

    }


    fun updateList(list: List<RestauranteEntity>){
        restaurantes = list
        notifyDataSetChanged()
    }

}