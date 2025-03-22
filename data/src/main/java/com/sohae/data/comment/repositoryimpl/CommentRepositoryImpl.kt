package com.sohae.data.comment.repositoryimpl

import android.util.Log
import com.sohae.common.models.comment.entity.CommentEntity
import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.comment.response.CommentResponse
import com.sohae.common.models.post.entity.PostId
import com.sohae.data.comment.mapper.toCommentEntity
import com.sohae.data.comment.mapper.toCreateCommentRequest
import com.sohae.data.comment.requestmethod.CommentRequestMethod
import com.sohae.domain.comment.reposiotory.CommentRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val retrofitWithToken: Retrofit
): CommentRepository {

    private val tag = "sohae_comment"

    private val client = retrofitWithToken.create(CommentRequestMethod::class.java)

    override fun createComment(
        commentEntity: CommentEntity,
        callback: (Boolean) -> Unit
    ) {
        val success = { callback(true) }
        val failure = { msg: String, e: Throwable? ->
            Log.e(tag, msg, e)
            callback(false)
        }
        val request = client.createComment(commentEntity.toCreateCommentRequest())

        request.enqueue(object : Callback<Unit>{
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

                if (p1.isSuccessful) {

                    if (p1.body() != null) {
                        success()
                    } else {
                        failure("서버 응답 없음", Throwable(p1.errorBody()?.string()))
                    }
                } else {
                    failure("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                }
            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })
    }

    override fun getCommentsList(
        postId: PostId,
        page: Int,
        callback: (List<CommentEntity>, Boolean) -> Unit
    ) {
        val success = { response: List<CommentResponse> ->
            callback(response.map { it.toCommentEntity() }, true)
        }
        val failure = { msg: String, e: Throwable? ->
            Log.e(tag, msg, e)
            callback(emptyList(), false)
        }
        val request = client.getCommentsList(postId, page)

        request.enqueue(object : Callback<List<CommentResponse>>{
            override fun onResponse(
                p0: Call<List<CommentResponse>>,
                p1: Response<List<CommentResponse>>
            ) {

                if (p1.isSuccessful) {

                    if (p1.body() != null) {
                        success(p1.body()!!)
                    } else {
                        failure("서버 응답 없음", Throwable(p1.errorBody()?.string()))
                    }
                } else {
                    failure("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                }
            }

            override fun onFailure(p0: Call<List<CommentResponse>>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })
    }

    override fun deleteComment(
        commentId: CommentId,
        callback: (Boolean) -> Unit
    ) {
        val success = { callback(true) }
        val failure = { msg: String, e: Throwable? ->
            Log.e(tag, msg, e)
            callback(false)
        }
        val request = client.deleteComment(commentId)

        request.enqueue(object : Callback<Unit>{
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

                if (p1.isSuccessful) {

                    if (p1.body() != null) {
                        success()
                    } else {
                        failure("서버 응답 없음", Throwable(p1.errorBody()?.string()))
                    }
                } else {
                    failure("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                }
            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                failure("서버 요청 실패", p1)
            }
        })
    }
}