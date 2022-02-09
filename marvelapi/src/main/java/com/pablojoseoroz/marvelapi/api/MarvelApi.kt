package com.pablojoseoroz.marvelapi.api

import com.pablojoseoroz.marvelapi.dto.CharacterDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    /**
     * Get branch
     */
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?
    ): Response<CharacterDataWrapper>

    /**
     * Get branch offers
     */
    @GET("v1/public/characters/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<CharacterDataWrapper>

}
