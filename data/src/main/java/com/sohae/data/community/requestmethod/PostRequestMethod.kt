package com.sohae.data.community.requestmethod

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.request.CreatePostRequest
import com.sohae.common.models.post.request.UpdatePostRequest
import com.sohae.common.models.post.response.PostResponse
import com.sohae.common.models.user.entity.UserId
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

private const val POST_URL = "posts"

interface PostRequestMethod {

    @POST(POST_URL)
    fun createPost(
        @Body postDetails: CreatePostRequest
    ): Call<Long>

    @GET(POST_URL)
    fun getPostsList(
        @Query("category_id") categoryId: CategoryId,
        @Query("page") page: Int
    ): Call<List<PostResponse>>

    @GET("user/{user_id}/post")
    fun getPostsListByUserId(
        @Path("user_id") userId: UserId,
        @Query("page") page: Int
    ): Call<List<PostResponse>>

    @GET("${POST_URL}/search")
    fun getPreviewPostsListByKeyword(
        @Query("keyword", encoded = true) keyword: String,
        @Query("page") page: Int
    ): Call<List<PostResponse>>

    @GET("${POST_URL}/popular")
    fun getPopularPostsList(
        @Query("period") periodIdx: Int,
        @Query("page") page: Int
    ): Call<List<PostResponse>>

    @GET("${POST_URL}/{postId}")
    fun getPostDetails(
        @Path("postId") postId: Long
    ): Call<PostResponse>

    @PUT("${POST_URL}/{postId}")
    fun updatePost(
        @Path("postId") postId: Long,
        @Body postDetails: UpdatePostRequest
    ): Call<Long>

    @DELETE("${POST_URL}/{postId}")
    fun deletePost(
        @Path("postId") postId: Long
    ): Call<Unit>
}