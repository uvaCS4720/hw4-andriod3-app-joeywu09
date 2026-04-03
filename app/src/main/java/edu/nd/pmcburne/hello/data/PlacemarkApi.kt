package edu.nd.pmcburne.hello.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PlacemarkApi {
    @GET("~wxt4gm/placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkDto>

    companion object {
        private const val BASE_URL = "https://www.cs.virginia.edu/"

        fun create(): PlacemarkApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlacemarkApi::class.java)
        }
    }
}
