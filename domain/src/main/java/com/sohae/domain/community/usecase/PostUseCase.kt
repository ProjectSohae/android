package com.sohae.domain.community.usecase

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.domain.community.repository.PostRepository
import javax.inject.Inject

class PostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    fun createPost(
        accessToken: String,
        postDetails: PostEntity,
        callBack: (Boolean) -> Unit
    ) {
        postRepository.createPost(accessToken, postDetails) {
            callBack(it)
        }
    }

    fun getPreviewPostsList(
        page: Int,
        categoryId: CategoryId,
        callBack: (List<PostEntity>) -> Unit
    ) {
        postRepository.getPreviewPostsList(page, categoryId) { previewPostsList, isSucceed ->
            callBack(previewPostsList)
        }
    }

    fun getPostDetails(
        postId: Long,
        callBack: (PostEntity?, Boolean) -> Unit
    ) {
        postRepository.getPostDetails(postId) { getPostEntity, errMsg ->

            if (errMsg.isBlank()) {
                callBack(getPostEntity, true)
            } else {
                callBack(getPostEntity, false)
            }
        }
    }

    fun updatePost(
        postDetails: PostEntity,
        callBack: (Boolean) -> Unit
    ) {
        postRepository.updatePost(postDetails) {
            callBack(it)
        }
    }

    fun deletePost(
        postId: Long,
        callBack: (Boolean) -> Unit
    ) {
        postRepository.deletePost(postId) {
            callBack(it)
        }
    }
}