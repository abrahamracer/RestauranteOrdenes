package com.avv.restauranteordenes.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.avv.restauranteordenes.R
import com.avv.restauranteordenes.application.RestauranteDBApp
import com.avv.restauranteordenes.data.RestauranteRepository
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var restaurantes: List<RestauranteEntity> = emptyList()
    private lateinit var repository: RestauranteRepository

    private lateinit var RestauranteAdapter: RestauranteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as RestauranteDBApp).repository


        RestauranteAdapter = RestauranteAdapter() { selectedRestaurante ->


            val dialog = RestauranteDialog(
                newComida = false,
                restaurante = selectedRestaurante,
                updateUI = {
                    updateUI()
                }, message = { action ->
                    message(action)
                })

            dialog.show(supportFragmentManager, "updateDialog")

        }

        binding.rvPlayers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = RestauranteAdapter
        }


        updateUI()

    }

    private fun updateUI() {
        lifecycleScope.launch {
            restaurantes = repository.getAllOrdenes()

            binding.tvNoRecords.visibility =
                if (restaurantes.isEmpty()) View.VISIBLE else View.INVISIBLE

            RestauranteAdapter.updateList(restaurantes)

        }
    }


    fun click(view: View) {

        val dialog = RestauranteDialog(
            updateUI = {
                updateUI()
            }, message = { action ->
                message(action)
            })
        dialog.show(supportFragmentManager, "insertDialog")
    }

    private fun message(text: String) {

        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()

    }

}