package com.example.gongik.view.post

import androidx.lifecycle.ViewModel
import com.example.gongik.model.data.post.CommentDetails
import com.example.gongik.model.data.post.PostDetails
import com.example.gongik.model.data.post.PostImage
import com.example.gongik.model.data.post.ReplyDetails
import com.example.gongik.model.data.user.UserInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostViewModel: ViewModel() {

    private var _postDetails = MutableStateFlow<PostDetails?>(null)
    val postDetails = _postDetails.asStateFlow()

    private var _commentsList = MutableStateFlow<List<CommentDetails>>(emptyList())
    val commentsList = _commentsList.asStateFlow()

    fun getPostDetails(postId: Int) {
        _postDetails.value = PostDetails(
            0,
            UserInformation(0, "테스트"),
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
                PostImage("", 0),
                PostImage("", 1),
                PostImage("", 2),
                PostImage("", 3),
                PostImage("", 4),
            ),
            0,
            0,
            0,
            0
        )
    }

    fun getCommentsList(postId: Int, offset: Int, count: Int) {
        val tmp = mutableListOf<CommentDetails>()

        for (idx: Int in 0..10) {
            tmp.add(
                CommentDetails(
                    0,
                    0,
                    UserInformation(
                        0,
                        "닉네임"
                    ),
                    0,
                    "내용\n내용내용\n내용내용내용\n내용내용내용내용내용내용내용내용내용내용내용내용",
                    5,
                    listOf(
                        ReplyDetails(
                            0,
                            0,
                            UserInformation(
                                0,
                                "닉네임2"
                            ),
                            UserInformation(
                                0,
                                "닉네임"
                            ),
                            0,
                            "내용 내용\n내용 내용 내용\n내용내용내용내용내용내용내용내용내용내용내용내용"
                        )
                    )
                )
            )
        }

        _commentsList.value = tmp.toList()
    }
}