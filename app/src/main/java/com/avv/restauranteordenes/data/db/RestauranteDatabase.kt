package com.avv.restauranteordenes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.avv.restauranteordenes.data.db.model.RestauranteEntity
import com.avv.restauranteordenes.util.Constants

@Database(
    entities = [RestauranteEntity::class],
    version = 1,
    exportSchema = true
)
abstract class RestauranteDatabase:RoomDatabase(){
    abstract fun restauranteDao(): RestauranteDao

    companion object{

        @Volatile
        private var INSTANCE: RestauranteDatabase? = null

        fun getDatabase(context: Context): RestauranteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestauranteDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
