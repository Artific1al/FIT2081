package com.monash.fit2081.allen_davies_33905428.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface MealService {

    @GET("search.php?s={query)")
    suspend fun getMeals(@Path("query") query: String): MealResponse


    // singleton network object to access api data
        companion object {
            var BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
            //query = "search.php?s={query}"


            fun create(): MealService {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                return retrofit.create(MealService::class.java)
            }
        }

    }





