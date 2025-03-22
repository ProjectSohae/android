package com.sohae.data.post.repositoryimpl

import android.util.Log
import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.response.PostResponse
import com.sohae.common.models.user.entity.UserId
import com.sohae.data.post.mapper.toCreatePostRequest
import com.sohae.data.post.mapper.toPostEntity
import com.sohae.data.post.mapper.toUpdatePostRequest
import com.sohae.data.post.requestmethod.PostRequestMethod
import com.sohae.domain.post.repository.PostRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URLEncoder
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val retrofitWithToken: Retrofit
): PostRepository {

    private val tag = "sohae_post"

    val client: PostRequestMethod = retrofitWithToken.create(PostRequestMethod::class.java)

    fun failureLog(msg: String, e: Throwable?) {
        Log.e(tag, msg, e)
    }

    override fun createPost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    ) {
        val success = {
            callback(true)
        }
        val failure = {
            callback(false)
        }
        val request = client.createPost(postDetails.toCreatePostRequest())

        request.enqueue(object: Callback<Long> {
            override fun onResponse(p0: Call<Long>, p1: Response<Long>) {

                if (p1.isSuccessful) {
                    success()
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<Long>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun getPreviewPostsList(
        page: Int,
        categoryId: CategoryId,
        callback: (List<PostEntity>, Boolean) -> Unit
    ) {
        val success: (List<PostResponse>) -> Unit = { response ->
            callback(
                response.map {
                    it.toPostEntity()
                },
                true
            )
        }
        val failure = {
            callback(emptyList(), false)
        }
        val request = client.getPostsList(categoryId, page)

        request.enqueue(object: Callback<List<PostResponse>> {
            override fun onResponse(
                p0: Call<List<PostResponse>>,
                p1: Response<List<PostResponse>>
            ) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure()
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<List<PostResponse>>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun getPreviewPopularPostsList(
        page: Int,
        periodIdx: Int,
        callback: (List<PostEntity>, Boolean) -> Unit
    ) {
        val success: (List<PostResponse>) -> Unit = { response ->
            callback(
                response.map {
                    it.toPostEntity()
                },
                true
            )
        }
        val failure = {
            callback(emptyList(), false)
        }
        val request = client.getPopularPostsList(periodIdx, page)

        request.enqueue(object: Callback<List<PostResponse>> {
            override fun onResponse(
                p0: Call<List<PostResponse>>,
                p1: Response<List<PostResponse>>
            ) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure()
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<List<PostResponse>>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun getPreviewPostsListByKeyword(
        page: Int,
        keyword: String,
        callback: (List<PostEntity>, Boolean) -> Unit
    ) {
        val success: (List<PostResponse>) -> Unit = { response ->
            callback(
                response.map {
                    it.toPostEntity()
                },
                true
            )
        }
        val failure = {
            callback(emptyList(), false)
        }
        val request = client.getPreviewPostsListByKeyword(
            URLEncoder.encode(keyword, "UTF-8"),
            page
        )

        request.enqueue(object: Callback<List<PostResponse>> {
            override fun onResponse(
                p0: Call<List<PostResponse>>,
                p1: Response<List<PostResponse>>
            ) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure()
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<List<PostResponse>>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun getPreviewPostsListByUserId(
        page: Int,
        userId: UserId,
        callback: (List<PostEntity>, Boolean) -> Unit
    ) {
        val success: (List<PostResponse>) -> Unit = { response ->
            callback(
                response.map {
                    it.toPostEntity()
                },
                true
            )
        }
        val failure = {
            callback(emptyList(), false)
        }
        val request = client.getPostsListByUserId(userId, page)

        request.enqueue(object: Callback<List<PostResponse>> {
            override fun onResponse(
                p0: Call<List<PostResponse>>,
                p1: Response<List<PostResponse>>
            ) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure()
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<List<PostResponse>>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun getPostDetails(
        postId: Long,
        callback: (PostEntity?) -> Unit
    ) {
        val success: (PostResponse) -> Unit = {
            callback(it.toPostEntity())
        }
        val failure: (String) -> Unit = {
            callback(null)
        }
        val request = client.getPostDetails(postId)

        request.enqueue(object: Callback<PostResponse> {
            override fun onResponse(p0: Call<PostResponse>, p1: Response<PostResponse>) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success(it)
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure("")
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure("")
                }
            }

            override fun onFailure(p0: Call<PostResponse>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure("")
            }
        })
    }

    override fun updatePost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    ) {
        val success = {
            callback(true)
        }
        val failure = {
            callback(false)
        }
        val request = client.updatePost(postDetails.id, postDetails.toUpdatePostRequest())

        request.enqueue(object: Callback<Long> {
            override fun onResponse(p0: Call<Long>, p1: Response<Long>) {

                if (p1.isSuccessful) {

                    p1.body()?.let {
                        success()
                    } ?: {
                        failureLog("서버 응답 내용 없음", Throwable(p1.errorBody()?.string()))
                        failure()
                    }
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<Long>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }

    override fun deletePost(
        postId: Long,
        callback: (Boolean) -> Unit
    ) {
        val success = {
            callback(true)
        }
        val failure = {
            callback(false)
        }
        val request = client.deletePost(postId)

        request.enqueue(object: Callback<Unit> {
            override fun onResponse(p0: Call<Unit>, p1: Response<Unit>) {

                if (p1.isSuccessful) {
                    success()
                } else {
                    failureLog("서버 응답 실패", Throwable(p1.errorBody()?.string()))
                    failure()
                }
            }

            override fun onFailure(p0: Call<Unit>, p1: Throwable) {
                failureLog("서버 요청 실패", p1)
                failure()
            }
        })
    }
}