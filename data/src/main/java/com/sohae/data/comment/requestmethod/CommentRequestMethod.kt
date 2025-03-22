package com.sohae.data.comment.requestmethod

import com.sohae.common.models.comment.entity.CommentId
import com.sohae.common.models.comment.request.CreateCommentRequest
import com.sohae.common.models.comment.response.CommentResponse
import com.sohae.common.models.post.entity.PostId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val COMMENT_URL = "comments"

interface CommentRequestMethod {

    @POST(COMMENT_URL)
    fun createComment(
        @Body request: CreateCommentRequest
    ): Call<Unit>

    @GET(COMMENT_URL)
    fun getCommentsList(
        @Query("post_id") postId: PostId,
        @Query("page") page: Int
    ): Call<List<CommentResponse>>

    @DELETE("$COMMENT_URL/{comment_id}")
    fun deleteComment(
        @Path("comment_id") commentId: CommentId
    ): Call<Unit>
}