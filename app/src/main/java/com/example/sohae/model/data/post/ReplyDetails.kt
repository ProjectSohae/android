package com.example.sohae.model.data.post

import com.example.sohae.model.data.user.UserInformation

data class ReplyDetails(
    val postId: Int,
    val commentId: Int,
    val userInfo: UserInformation,
    val targetUserInfo: UserInformation,

    val createdDate: Long,

    val content: String,
)