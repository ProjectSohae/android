package com.jhw.sohae.data.model.comment

data class CommentDetailsDTO(
    val id: Int,
    val postId: Int,
    val userNickname: String,
    val targetUserNickname: String,
    val parentCommentId: Int,

    val createdDate: Long,

    val content: String,

    val replyList: List<CommentDetailsDTO>,

    val isRemoved: Boolean
)