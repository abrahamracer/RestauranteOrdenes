package com.avv.restauranteordenes.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.avv.restauranteordenes.util.Constants


@Entity(tableName = Constants.DATABASE_RESTAURANTE_TABLE)
data class RestauranteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "orden_id")
    val id: Long = 0,
    @ColumnInfo(name = "orden_comida")
    var comida: String,
    @ColumnInfo(name = "orden_nombre")
    var nombre: String,
    @ColumnInfo(name = "orden_propina", defaultValue = "0")
    var propina: String
)