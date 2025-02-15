package com.jhw.sohae.presentation.post

import androidx.lifecycle.ViewModel
import com.jhw.sohae.data.model.comment.request.CommentDetailsDTO
import com.jhw.sohae.data.model.post.PostDetailsDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostViewModel: ViewModel() {

    private var _postDetails = MutableStateFlow<PostDetailsDTO?>(null)
    val postDetails = _postDetails.asStateFlow()

    private var _commentsList = MutableStateFlow<List<CommentDetailsDTO>>(emptyList())
    val commentsList = _commentsList.asStateFlow()

    fun getPostDetails(postId: Int) {
        _postDetails.value = PostDetailsDTO(
            0,
            "닉네임",
            0,
            0,
            0,
            "제목",
            "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용\n" +
                    "내용",
            listOf(
//                PostImage("", 0),
//                PostImage("", 1),
//                PostImage("", 2),
//                PostImage("", 3),
//                PostImage("", 4),
            ),
            0,
            0,
            0,
            0
        )
    }

    fun getCommentsList(postId: Int, offset: Int, count: Int) {
        val tmp = mutableListOf<CommentDetailsDTO>()

        for (idx: Int in 0..10) {
            tmp.add(
                CommentDetailsDTO(
                    0,
                    0,
                    "댓글러",
                    "타겟",
                    0,
                    0,
                    "내용\n내용내용\n내용내용내용\n내용내용내용내용내용내용내용내용내용내용내용내용",
                    listOf(
                        CommentDetailsDTO(
                            0,
                            0,
                            "대댓글러",
                            "타겟",
                            0,
                            0,
                            "내용 내용\n내용 내용 내용\n내용내용내용내용내용내용내용내용내용내용내용내용",
                            emptyList(),
                            false
                        )
                    ),
                    false
                )
            )
        }

        _commentsList.value = tmp.toList()
    }
}