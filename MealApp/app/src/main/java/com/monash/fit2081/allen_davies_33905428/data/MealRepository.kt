package com.monash.fit2081.allen_davies_33905428.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealRepository() {

    private val apiService = MealService.create()

    //get APIResponse
    suspend fun getResponse(query: String): MealResponse{
        return apiService.getMeals(query)

    }




}
