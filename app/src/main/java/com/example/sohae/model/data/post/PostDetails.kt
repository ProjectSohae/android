package com.example.sohae.model.data.post

import com.example.sohae.model.data.user.UserInformation

data class PostDetails(
    val postId: Int,
    val userInfo: UserInformation,

    val createdDate: Long,
    val modifiedDate: Long,

    val title: String,
    val content: String,
    val imagesList: List<PostImage>,

    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val bookmarkCount: Int
)