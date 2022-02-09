package com.pablojoseoroz.marvel.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla donde se guardarán los personajes favoritos identificados por su Id
 *
 * @property id
 * @constructor Create empty Character favorite
 */
@Entity(tableName = "Favorites")
data class CharacterFavorite(@PrimaryKey val id: Int)