package com.sohae.domain.community.repository

import com.sohae.common.models.post.entity.CategoryId
import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.user.entity.UserId

interface PostRepository {

    fun createPost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    )

    fun getPreviewPostsList(
        page: Int,
        categoryId: CategoryId,
        callback: (List<PostEntity>, Boolean) -> Unit
    )

    fun getPreviewPopularPostsList(
        page: Int,
        periodIdx: Int,
        callback: (List<PostEntity>, Boolean) -> Unit
    )

    fun getPreviewPostsListByKeyword(
        page: Int,
        keyword: String,
        callback: (List<PostEntity>, Boolean) -> Unit
    )
    
    fun getPreviewPostsListByUserId(
        page: Int,
        userId: UserId,
        callback: (List<PostEntity>, Boolean) -> Unit
    )

    fun getPostDetails(
        postId: Long,
        callback: (PostEntity?) -> Unit
    )

    fun updatePost(
        postDetails: PostEntity,
        callback: (Boolean) -> Unit
    )

    fun deletePost(
        postId: Long,
        callback: (Boolean) -> Unit
    )
}