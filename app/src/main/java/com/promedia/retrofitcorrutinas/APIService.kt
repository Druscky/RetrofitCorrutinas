package com.promedia.retrofitcorrutinas

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

// Creamos el método por el cual accedemos a nuestra API
interface APIService {

    @GET // Defino el tipo de llamada
    suspend fun getDogsByBreeds(@Url url:String):Response<DogsResponse>

}