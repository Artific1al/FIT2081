package com.monash.fit2081.allen_davies_33905428.ui

//w7 lab
sealed interface UiState {

    //first render
    object Initial: UiState

    //still loading
    object Loading: UiState

    //gen ai response complete
    data class Success(val outputText: String): UiState

    data class Error(val errorMessage: String): UiState

}