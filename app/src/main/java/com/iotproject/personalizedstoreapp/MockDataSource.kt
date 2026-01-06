package com.iotproject.personalizedstoreapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object DataSource {

    private const val BASE_URL = "http://192.168.0.191:5001/"

    private val api: StoreApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreApi::class.java)
    }

    suspend fun getProducts(uuid: String, major: String, minor: String, mode: String): List<Product> {
        val floor = major.toIntOrNull() ?: return emptyList()
        val aisleId = minor.toIntOrNull() ?: return emptyList()
        val safeMode = if (mode.equals("promo", ignoreCase = true)) "promo" else "all"

        return api.getProducts(floor, aisleId, safeMode)
    }
}

interface StoreApi {
    @GET("api/products")
    suspend fun getProducts(
        @Query("floor") floor: Int,
        @Query("aisle_id") aisleId: Int,
        @Query("mode") mode: String
    ): List<Product>
}

data class Product(
    val product_id: Int,
    val product_name: String,
    val product_description: String?,
    val product_image: String?,
    val product_location: Int?,
    val discount: String?,
    val aisle_name: String?
)
