package com.al.s33905428.socialmedia.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "posts")
data class Post(

    @PrimaryKey
    val postId: String,

    val userName: String,

    val subject: String,

    val content: String,

    val createdAt: String
)