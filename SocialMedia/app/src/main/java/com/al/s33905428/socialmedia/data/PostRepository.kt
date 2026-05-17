package com.al.s33905428.socialmedia.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.jvm.java

class PostRepository( private val applicationContext: Context){
    val database = AppDatabase.getDatabase(applicationContext)
    val postDao = database.postDao()



    private val retrofit = Retrofit.Builder()
        .baseUrl("https://week9-twitter-api-323733522796.australia-southeast1.run.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(TweetApiService::class.java)

    interface TweetApiService {
        /**
         * GET endpoint to retrieve all tweets from the server.
         *
         * @return List<Post> containing all posts from the API
         */
        @GET("api/tweets")
        suspend fun getTweets(): List<Post>

        /**
         * POST endpoint to create a new tweet on the server.
         *
         * @return Post the newly created post returned by the API
         */
        @POST("api/tweets/new")
        suspend fun createTweet(): Post
    }


    suspend fun getAllPosts(): List<Post> {
        return try {
            if (isNetworkAvailable()) {
                try {
                    // Fetch from API if online
                    val apiPosts = withContext(Dispatchers.IO) {
                        apiService.getTweets()
                    }
                    // Convert API response to Post entities and insert into database
                    val posts = apiPosts.map { apiPost ->
                        Post(
                            postId = apiPost.postId ?: "",
                            userName = apiPost.userName ?: "Unknown User",
                            subject = apiPost.subject ?: "No Subject",
                            content = apiPost.content ?: "",
                            createdAt = apiPost.createdAt?:""
                        )
                    }
                    // Save API posts to local database for offline access
                    if (posts.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            posts.forEach { post ->
                                postDao.insertPost(post)
                            }
                        }
                    }
                    posts
                } catch (e: Exception) {
                    // If API call fails, fall back to local data
                    e.printStackTrace()
                    withContext(Dispatchers.IO) {
                        postDao.getAllPosts().first()
                    }
                }
            } else {
                // If offline, return local data
                withContext(Dispatchers.IO) {
                    postDao.getAllPosts().first()
                }
            }
        } catch (e: Exception) {
            // If everything fails, at least return an empty list
            e.printStackTrace()
            emptyList()
        }
    }

    fun isNetworkAvailable(): Boolean {
        // Get the ConnectivityManager system service
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Check if the device has an active network
        val network = connectivityManager.activeNetwork ?: return false
        // Get the network capabilities for the active network
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        // Check if the network has any of the following transports:
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    suspend fun deleteAllPosts() {
        withContext(Dispatchers.IO) {
            postDao.deleteAllPosts()
        }
    }

    suspend fun createPost(): Boolean {
        return try {
            if (isNetworkAvailable()) {
                // Make API call to create a post on the server
                val apiResponse = withContext(Dispatchers.IO) {
                    apiService.createTweet()
                }
                // Refresh posts to include the newly created one
                getAllPosts()
                return true
            } else {
                // If offline, create a local post with random content
                val randomPost = createLocalPost()
                postDao.insertPost(randomPost)
                // Refresh posts to include the newly created one
                getAllPosts()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    private fun createLocalPost(): Post {
        // Define date formatters for consistent formatting
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")
        val now = LocalDateTime.now()
        // Format the current date-time
        val formatDateTime = now.format(formatter)

        // Sample data collections for random generation
        val userNames = listOf(
            "John Smith", "Emma Wilson", "Michael Brown",
            "Sophia Johnson", "Robert Davis", "Olivia Jones"
        )
        val subjects = listOf("Local Post")
        val contentTemplates = listOf(
            "Just had an amazing experience with %s!",
            "Does anyone else think %s is overrated?",
            "I can't believe what's happening in %s right now",
            "Looking for recommendations about %s",
            "My thoughts on %s: absolutely fascinating!",
            "Today I learned something new about %s"
        )

        // Generate the post content
        val selectedSubject = subjects.random()
        val content = contentTemplates.random().format(selectedSubject.lowercase())

        // Create and return the post with a unique ID
        return Post(
            postId = UUID.randomUUID().toString(),
            userName = userNames.random(),
            subject = selectedSubject,
            content = content,
            createdAt = formatDateTime
        )
    }


}