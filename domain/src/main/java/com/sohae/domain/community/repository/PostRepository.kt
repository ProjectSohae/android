package com.sohae.domain.community.repository

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity

interface PostRepository {

    fun createPost(
        postDetails: PostEntity,
        callBack: (Boolean) -> Unit
    )

    fun getPreviewPostsList(
        page: Int,
        categoryId: CategoryId,
        callBack: (List<PostEntity>, Boolean) -> Unit
    )

    fun getPostDetails(
        postId: Long,
        callBack: (PostEntity?) -> Unit
    )

    fun updatePost(
        postDetails: PostEntity,
        callBack: (Boolean) -> Unit
    )

    fun deletePost(
        postId: Long,
        callBack: (Boolean) -> Unit
    )
}