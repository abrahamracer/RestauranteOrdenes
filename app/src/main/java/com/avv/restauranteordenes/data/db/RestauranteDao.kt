package com.avv.restauranteordenes.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.util.Constants


@Dao
interface RestauranteDao {

    @Insert
    suspend fun insertOrden(restaurante : RestauranteEntity)

    @Insert
    suspend fun insertOrden(restaurantes : List<RestauranteEntity>)

    @Query("SELECT * FROM ${Constants.DATABASE_RESTAURANTE_TABLE}")
    suspend fun getAllOrdenes(): List<RestauranteEntity>

    @Update
    suspend fun updateOrden(restaurante: RestauranteEntity)

    @Delete
    suspend fun deleteOrden(restaurante: RestauranteEntity)

}