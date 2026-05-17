package com.monash.fit2081.allen_davies_33905428.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monash.fit2081.allen_davies_33905428.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealViewModel : ViewModel(){

    //represents the current state
    private val currentState: MutableStateFlow<UiState>
            = MutableStateFlow(UiState.Initial)

    //public, immuteable state for observing current state
    val uiState: StateFlow<UiState>
            = currentState.asStateFlow()

    fun searchMeal(query: String): MealResponse{
       val emptyJson = emptyList<JSONResponse>()
       var response = MealResponse(emptyJson)
        viewModelScope.launch {
            response = mealRespository.getResponse(query)
        }


        return response
    }

    //access to model
    private val mealRespository: MealRepository = MealRepository()

    //MealViewModelFactory
    class MealViewModelFactory(context: Context) : ViewModelProvider.Factory {

        override fun <T: ViewModel> create(modelClass: Class<T>): T
                = MealViewModel() as T
    }

}