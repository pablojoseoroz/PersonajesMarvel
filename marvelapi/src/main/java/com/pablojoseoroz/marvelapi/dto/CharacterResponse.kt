package com.pablojoseoroz.marvelapi.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val data: CharacterDataContainer,
    val etag: String
): Parcelable

@Parcelize
data class CharacterDataContainer(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<Character>
): Parcelable

@Parcelize
data class Character(
    val id: Int, val name: String, val description: String, val modified: String,
    val resourceURI: String,
    val urls: List<Url>,
    val thumbnail: Image,
    val comics: ResourceList,
    val stories: StoryList,
    val events: ResourceList,
    val series: ResourceList
): Parcelable

@Parcelize
data class Image(
    val path: String,
    val extension: String
): Parcelable

@Parcelize
data class Url(val type: String, val url: String): Parcelable

@Parcelize
data class ResourceList(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<ResourceSummary>
): Parcelable

@Parcelize
data class StoryList(
    val available: Int, val returned: Int, val collectionURI: String, val items: List<ResourceSummary>
): Parcelable

@Parcelize
data class ResourceSummary(val resourceURI: String, val name: String): Parcelable

@Parcelize
data class StorySummary(val resourceURI: String, val name: String, val type: String): Parcelable