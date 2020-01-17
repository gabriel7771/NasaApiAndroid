package com.jgba.nasaapiandroid.model


import com.google.gson.annotations.SerializedName

data class Collection(
    @SerializedName("href")
    val href: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("links")
    val links: List<LinkX>,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("version")
    val version: String
)