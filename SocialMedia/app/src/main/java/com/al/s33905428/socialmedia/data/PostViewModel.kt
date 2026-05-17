package com.al.s33905428.socialmedia.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class PostViewModel(application: Application) : AndroidViewModel(application){

    private val repository: PostRepository = PostRepository(application.applicationContext)

    private val _allPosts = MutableStateFlow<List<Post>>(emptyList())

    val allPosts: StateFlow<List<Post>>
        get() = _allPosts.asStateFlow()

    init {
        refreshPosts()
    }

    fun generateRandomPost(){
        viewModelScope.launch(Dispatchers.IO){
            repository.createPost()
        }
    }

    fun refreshPosts(){
        viewModelScope.launch{
            _allPosts.value = repository.getAllPosts()
            println("Posts refreshed: ${_allPosts.value.size} posts loaded")
        }
    }

    fun isNetworkAvailable(): Boolean{
        return repository.isNetworkAvailable()
    }

    fun deleteAllPosts(){
        viewModelScope.launch{
            repository.deleteAllPosts()
            refreshPosts()
        }
    }
}