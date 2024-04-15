package com.avv.restauranteordenes.data

import com.avv.restauranteordenes.data.db.RestauranteDao
import com.avv.restauranteordenes.data.db.model.RestauranteEntity


class RestauranteRepository(private val restauranteDao: RestauranteDao) {

    suspend fun insertOrden(Restaurante: RestauranteEntity){
        restauranteDao.insertOrden(Restaurante)
    }

    suspend fun getAllOrdenes(): List<RestauranteEntity>{
        return restauranteDao.getAllOrdenes()
    }

    suspend fun updateOrden(Restaurante: RestauranteEntity){
        restauranteDao.updateOrden(Restaurante)
    }

    suspend fun deleteOrden(Restaurante: RestauranteEntity){
        restauranteDao.deleteOrden(Restaurante)
    }

}