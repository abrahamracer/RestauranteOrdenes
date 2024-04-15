package com.avv.restauranteordenes.application

import android.app.Application
import com.avv.restauranteordenes.data.RestauranteRepository
import com.avv.restauranteordenes.data.db.RestauranteDatabase

class RestauranteDBApp:Application() {
    private val database by lazy {
        RestauranteDatabase.getDatabase(this@RestauranteDBApp)
    }
    val repository by lazy {
        RestauranteRepository(database.restauranteDao())
    }
}

