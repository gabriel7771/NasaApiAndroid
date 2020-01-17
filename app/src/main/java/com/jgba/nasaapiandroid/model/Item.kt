package com.jgba.nasaapiandroid.model


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("href")
    val href: String,
    @SerializedName("links")
    val links: List<Link>
)