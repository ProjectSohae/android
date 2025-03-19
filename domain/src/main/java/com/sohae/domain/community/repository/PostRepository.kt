package com.sohae.domain.community.repository

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId

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

    fun getPreviewPopularPostsList(
        page: Int,
        periodIdx: Int,
        callBack: (List<PostEntity>, Boolean) -> Unit
    )

    fun getPreviewPostsListByUserId(
        page: Int,
        userId: UserId,
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