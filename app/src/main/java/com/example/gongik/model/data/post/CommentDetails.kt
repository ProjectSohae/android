package com.example.gongik.model.data.post

import com.example.gongik.model.data.user.UserInformation

data class CommentDetails(
    val postId: Int,
    val commentId: Int,
    val userInfo: UserInformation,

    val createdDate: Long,

    val content: String,

    val repliesCount: Int,
    val repliesList: List<ReplyDetails>
)