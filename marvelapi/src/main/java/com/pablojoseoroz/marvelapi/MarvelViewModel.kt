package com.pablojoseoroz.marvelapi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.pablojoseoroz.marvelapi.dto.CharacterDataWrapper
import com.pablojoseoroz.marvelapi.requests.Parameters
import kotlinx.coroutines.Dispatchers
import com.pablojoseoroz.marvelapi.responses.Result

/**
 * Marvel view model connect with [MarvelRepository].
 *
 * Use Coroutines on [Dispatchers.IO] to consume all repository calls.
 * All calls use same logic:
 * 1) Create a [liveData] on [Dispatchers.IO]
 * 2) Emit loading status before call [MarvelRepository]
 * 3) Call functions corresponding call
 * 4) If succes emit success with result
 * 5) If failure emit error with throwable
 *
 * @constructor
 *
 * @param application
 */
class MarvelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarvelRepository
     val parameters : Parameters
    val defaultOffset : Int

    init {
        repository = MarvelRepository(application)
        defaultOffset = 20
        parameters = Parameters(20, defaultOffset)
    }

    fun fetchCharacters() =
        liveData<Result<CharacterDataWrapper>>(Dispatchers.IO) {
            runCatching {
                emit(Result.Loading())
                repository.fetchCharacters(parameters)
            }.onSuccess {
                emit(Result.Success(it.body()))
            }.onFailure {
                it.printStackTrace()
                emit(Result.Error(it))
            }
        }

    fun getCharacter(id: Int) =
        liveData<Result<CharacterDataWrapper>>(Dispatchers.IO) {
            runCatching {
                emit(Result.Loading())
                repository.getCharacter(id)
            }.onSuccess {
                emit(Result.Success(it.body()))
            }.onFailure {
                it.printStackTrace()
                emit(Result.Error(it))
            }
        }

}