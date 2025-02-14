package com.jhw.sohae.data.model.post

data class PostDetailsDTO(
    val postId: Int,
    val userNickname: String,

    val createdDate: Long,
    val modifiedDate: Long,

    val categoryId: Int,
    val title: String,
    val content: String,
    val imagesList: List<PostImageDTO>,

    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val bookmarkCount: Int
)