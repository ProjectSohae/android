package com.sohae.data.community.mapper

import com.sohae.common.models.post.entity.PostEntity
import com.sohae.common.models.post.entity.PostImageEntity
import com.sohae.common.models.post.request.CreatePostRequest
import com.sohae.common.models.post.request.UpdatePostRequest
import com.sohae.common.models.post.response.PostResponse
import kotlinx.datetime.Instant

fun PostEntity.toCreatePostRequest(): CreatePostRequest = CreatePostRequest(
    this.categoryId,
    this.title,
    this.content,
    this.images.map { it.url }
)

fun PostEntity.toUpdatePostRequest(): UpdatePostRequest = UpdatePostRequest(
    this.categoryId,
    this.title,
    this.content,
    this.images.map { it.url }
)

fun PostResponse.toPostEntity(): PostEntity = PostEntity(
    this.id,
    this.categoryId,
    this.author.id,
    this.author.nickname,
    this.title,
    this.content,
    this.images.map { PostImageEntity(it) },
    this.commentCount,
    this.viewsCount,
    this.likesCount,
    this.bookmarksCount,
    Instant.fromEpochMilliseconds(this.createdAt),
    Instant.fromEpochMilliseconds(this.updatedAt)
)