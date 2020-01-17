package com.jgba.nasaapiandroid.model


import com.google.gson.annotations.SerializedName

data class ModelJSON(
    @SerializedName("collection")
    val collection: Collection
)