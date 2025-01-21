package com.example.gongik.model.data.previewpost

import com.example.gongik.model.data.user.UserInformation

data class PreviewPostDetails(
    val postId: Int,
    val userInfo: UserInformation,

    val createdDate: Long,
    val modifiedDate: Long,

    val title: String,
    val content: String,
    val thumbnailUrl: String,

    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int
)
