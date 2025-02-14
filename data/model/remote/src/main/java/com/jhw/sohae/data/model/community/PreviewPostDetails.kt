package com.jhw.sohae.data.model.community

data class PreviewPostDetails(
    val postId: Int,
    val userNickname: String,

    val createdDate: Long,
    val modifiedDate: Long,

    val categoryId: Int,
    val title: String,
    val content: String,
    val thumbnailUrl: String,

    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int
)
