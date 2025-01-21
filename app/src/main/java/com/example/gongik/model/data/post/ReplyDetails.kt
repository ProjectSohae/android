package com.example.gongik.model.data.post

import com.example.gongik.model.data.user.UserInformation

data class ReplyDetails(
    val postId: Int,
    val commentId: Int,
    val userInfo: UserInformation,
    val targetUserInfo: UserInformation,

    val createdDate: Long,

    val content: String,
)