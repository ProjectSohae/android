package com.sohae.domain.post.usecase

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId
import com.sohae.domain.post.repository.PostRepository
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    fun createPost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    ) {
        postRepository.createPost(postDetails) {
            callback(it)
        }
    }

    fun getPreviewPostsList(
        page: Int,
        categoryId: CategoryId,
        callback: (List<PostEntity>) -> Unit
    ) {
        postRepository.getPreviewPostsList(
            page,
            categoryId
        ) { previewPostsList, isSucceed ->
            callback(previewPostsList)
        }
    }

    fun getPreviewPostsListByUserId(
        page: Int,
        userId: UserId,
        callback: (List<PostEntity>) -> Unit
    ) {
        postRepository.getPreviewPostsListByUserId(
            page,
            userId
        ) { previewPostsList, isSucceed ->
            callback(previewPostsList)
        }
    }

    fun getPreviewPostsListByKeyword(
        page: Int,
        keyword: String,
        callback: (List<PostEntity>) -> Unit
    ) {
        postRepository.getPreviewPostsListByKeyword(
            page,
            keyword
        ) { previewPostsList, isSucceed ->
            callback(previewPostsList)
        }
    }

    fun getPreviewPopularPostsList(
        page: Int,
        periodIdx: Int,
        callback: (List<PostEntity>) -> Unit
    ) {
        postRepository.getPreviewPopularPostsList(
            page,
            periodIdx
        ) { previewPostsList, isSucceed ->
            callback(previewPostsList)
        }
    }

    fun getPostDetails(
        postId: Long,
        callback: (PostEntity?, Boolean) -> Unit
    ) {
        postRepository.getPostDetails(
            postId
        ) { getPostEntity ->

            if (getPostEntity != null) {
                callback(getPostEntity, true)
            } else {
                callback(null, false)
            }
        }
    }

    fun updatePost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    ) {
        postRepository.updatePost(postDetails) {
            callback(it)
        }
    }

    fun deletePost(
        postId: Long,
        callback: (Boolean) -> Unit
    ) {
        postRepository.deletePost(postId) {
            callback(it)
        }
    }
}