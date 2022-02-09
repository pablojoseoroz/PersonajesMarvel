package com.pablojoseoroz.marvel.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharacterFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(CharacterFavorites: CharacterFavorite)

    @Delete
    fun delete(user: CharacterFavorite)

    @Query("SELECT * FROM Favorites")
    fun getAll(): List<CharacterFavorite>

    @Query("SELECT EXISTS (SELECT 1 FROM Favorites WHERE id = :id)")
    fun isFavorite(id: Int): Boolean?

}