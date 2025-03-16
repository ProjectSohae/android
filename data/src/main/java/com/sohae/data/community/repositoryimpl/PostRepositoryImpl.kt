package com.sohae.data.community.repositoryimpl

import android.util.Log
import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.response.PostResponse
import com.sohae.data.community.mapper.toCreatePostRequest
import com.sohae.data.community.mapper.toPostEntity
import com.sohae.data.community.mapper.toUpdatePostRequest
import com.sohae.data.community.requestmethod.PostRequestMethod
import com.sohae.domain.community.repository.PostRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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
        callBack: (Boolean) -> Unit
    ) {
        val success = {
            callBack(true)
        }
        val failure = {
            callBack(false)
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
        callBack: (List<PostEntity>, Boolean) -> Unit
    ) {
        val success: (List<PostResponse>) -> Unit = { response ->
            callBack(
                response.map {
                    it.toPostEntity()
                },
                true
            )
        }
        val failure = {
            callBack(emptyList(), false)
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

    override fun getPostDetails(
        postId: Long,
        callBack: (PostEntity?) -> Unit
    ) {
        val success: (PostResponse) -> Unit = {
            callBack(it.toPostEntity())
        }
        val failure: (String) -> Unit = {
            callBack(null)
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
        callBack: (Boolean) -> Unit
    ) {
        val success = {
            callBack(true)
        }
        val failure = {
            callBack(false)
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
        callBack: (Boolean) -> Unit
    ) {
        val success = {
            callBack(true)
        }
        val failure = {
            callBack(false)
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