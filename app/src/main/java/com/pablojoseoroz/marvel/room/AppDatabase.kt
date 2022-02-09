package com.pablojoseoroz.marvel.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * App database de Room encargado de los personajes favoritos
 *
 * @constructor Create empty App database
 */
@Database(entities = [CharacterFavorite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun payloadDao(): CharacterFavoriteDao
}