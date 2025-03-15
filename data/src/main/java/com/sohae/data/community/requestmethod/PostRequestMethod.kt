package com.sohae.data.community.requestmethod

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.request.CreatePostRequest
import com.sohae.common.models.post.request.UpdatePostRequest
import com.sohae.common.models.post.response.PostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRequestMethod {

    @POST("posts")
    fun createPost(
        @Body postDetails: CreatePostRequest
    ): Call<Long>

    @GET("posts")
    fun getPostsList(
        @Query("category_id") categoryId: CategoryId,
        @Query("page") page: Int
    ): Call<List<PostResponse>>

    @GET("posts/{postId}")
    fun getPostDetails(
        @Path("postId") postId: Long
    ): Call<PostResponse>

    @PUT("posts/{postId}")
    fun updatePost(
        @Path("postId") postId: Long,
        @Body postDetails: UpdatePostRequest
    ): Call<Long>

    @DELETE("posts/{postId}")
    fun deletePost(
        @Path("postId") postId: Long
    ): Call<Unit>
}