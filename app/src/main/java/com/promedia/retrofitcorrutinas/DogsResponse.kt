package com.promedia.retrofitcorrutinas

import com.google.gson.annotations.SerializedName

// La variables deben llamarse igual que la API
data class DogsResponse (
    // Pero puedo llamarlas como quiera con Serializadores
    @SerializedName("status") var status: String,
    @SerializedName("message") var images: List<String>)