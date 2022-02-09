package com.pablojoseoroz.marvel.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.pablojoseoroz.marvel.room.AppDatabase
import com.pablojoseoroz.marvel.room.CharacterFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Favorite view model para encargarse de las operaciones de los personajes favoritos
 *
 * @constructor
 *
 * @param app
 */
class FavoriteViewModel(app : Application) : AndroidViewModel(app) {

    private var mDatabase: AppDatabase? = null

    init {
        mDatabase = Room.databaseBuilder(
            getApplication(), AppDatabase::class.java, "database-marvel"
        ).build()
    }

    fun getFavorites(): MutableLiveData<List<CharacterFavorite>> {
        val mutableLiveData = MutableLiveData<List<CharacterFavorite>>()
        viewModelScope.launch(Dispatchers.IO) {
            mutableLiveData.postValue(mDatabase?.payloadDao()?.getAll())
        }
        return mutableLiveData
    }

    fun isFavorite(id: Int): MutableLiveData<Boolean> {
        val mutableLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            mutableLiveData.postValue(mDatabase?.payloadDao()?.isFavorite(id))
        }
        return mutableLiveData
    }

    fun addFavorite(id: Int): MutableLiveData<Boolean> {
        val mutableLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            mDatabase?.payloadDao()?.add(CharacterFavorite(id))
            mutableLiveData.postValue(true)
        }
        return mutableLiveData
    }

    fun removeFavorite(id: Int): MutableLiveData<Boolean> {
        val mutableLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            mDatabase?.payloadDao()?.delete(CharacterFavorite(id))
            mutableLiveData.postValue(false)
        }
        return mutableLiveData
    }
}