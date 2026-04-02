package com.newBie.new_bie.features.post.data.mapper

import com.newBie.new_bie.features.post.data.dto.CategoryDto
import com.newBie.new_bie.features.post.data.dto.CategoryTypeDto
import com.newBie.new_bie.features.post.data.dto.CategoryTypeDtoWithSupabase
import com.newBie.new_bie.features.post.data.dto.CommentWithProfileDto
import com.newBie.new_bie.features.post.data.dto.CommentsDto
import com.newBie.new_bie.features.post.data.dto.LikesCountDto
import com.newBie.new_bie.features.post.data.dto.LikesDto
import com.newBie.new_bie.features.post.data.dto.NoticeDto
import com.newBie.new_bie.features.post.data.dto.PostDto
import com.newBie.new_bie.features.post.data.dto.PostImageDto
import com.newBie.new_bie.features.post.data.dto.PostUserDto
import com.newBie.new_bie.features.post.data.dto.PostWithProfileDto
import com.newBie.new_bie.features.post.data.dto.ReportPostDto
import com.newBie.new_bie.features.post.data.dto.SearchResultDto
import com.newBie.new_bie.features.post.data.dto.UserDto
import com.newBie.new_bie.features.post.data.dto.UserDtoWithSupabase
import com.newBie.new_bie.features.post.domain.entities.CategoryEntity
import com.newBie.new_bie.features.post.domain.entities.CategoryTypeEntity
import com.newBie.new_bie.features.post.domain.entities.CommentWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.CommentsEntity
import com.newBie.new_bie.features.post.domain.entities.LikesCountEntity
import com.newBie.new_bie.features.post.domain.entities.LikesEntity
import com.newBie.new_bie.features.post.domain.entities.NoticeEntity
import com.newBie.new_bie.features.post.domain.entities.PostEntity
import com.newBie.new_bie.features.post.domain.entities.PostImageEntity
import com.newBie.new_bie.features.post.domain.entities.PostUserEntity
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.features.post.domain.entities.ReportPostEntity
import com.newBie.new_bie.features.post.domain.entities.SearchResultEntity
import com.newBie.new_bie.features.post.domain.entities.UserEntity

fun PostWithProfileDto.toEntity(): PostWithProfileEntity {
    return PostWithProfileEntity(
        id = id,
        title = title,
        content = content,
        imageUrl = imageUrl,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        createdAt = createdAt,
        isBlock = isBlock,
        likesCount = likesCount,
        commentsCount = commentsCount,
        isLiked = isLiked,
        user = user?.toEntity(),
        postImages = postImages.map { it.toEntity() },
        categories = categories.map { it.toEntity() }
    )
}

fun PostUserDto.toEntity(): PostUserEntity {
    return PostUserEntity(
        id = id,
        nickName = nickName,
        profileImage = profileImage
    )
}

fun PostImageDto.toEntity(): PostImageEntity {
    return PostImageEntity(
        id = id,
        index = index,
        imageUrl = imageUrl,
        createdAt = createdAt
    )
}

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        categoryType = categoryType.toEntity()
    )
}

fun CategoryTypeDtoWithSupabase.toEntity(): CategoryTypeEntity {
    return CategoryTypeEntity(
        id = id,
        typeTitle = typeTitle
    )
}

fun CategoryTypeDto.toEntity(): CategoryTypeEntity {
    return CategoryTypeEntity(
        id = id,
        typeTitle = typeTitle
    )
}

fun CommentsDto.toEntity(): CommentsEntity {
    return CommentsEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt
    )
}

fun CommentWithProfileDto.toEntity(): CommentWithProfileEntity {
    return CommentWithProfileEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        deletedAt = deletedAt,
        createdAt = createdAt,
        isBlock = isBlock,
        user = user.toEntity(),
    )
}

fun LikesCountDto.toEntity(): LikesCountEntity{
    return LikesCountEntity(
        likesCount = likesCount
    )
}

fun LikesDto.toEntity(): LikesEntity{
    return LikesEntity(
        id = id,
        postId = postId,
        userId = userId,
        createdAt = createdAt
    )
}

fun NoticeDto.toEntity(): NoticeEntity{
    return NoticeEntity(
        id = id,
        title = title,
        content = content,
        publishedAt = publishedAt,
        createdAt = createdAt
    )
}

fun PostDto.toEntity(): PostEntity{
    return PostEntity(
        id = id,
        authorId = authorId,
        title = title,
        content = content,
        imageUrl = imageUrl,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        createdAt = createdAt,
        isBlock = isBlock,
        likesCount = likesCount,
        commentsCount = commentsCount
    )
}

fun ReportPostDto.toEntity(): ReportPostEntity{
    return ReportPostEntity(
        id = id,
        postId = postId,
        reporterId = reporterId,
        reason = reason,
        createdAt = createdAt,
        reviewedAt = reviewedAt
    )
}

fun SearchResultDto.toEntity(): SearchResultEntity{
    return SearchResultEntity(
        keyword = keyword,
        type = type,
        posts = posts.map { it.toEntity() },
        users = users.map { it.toEntity() },
        totalCount = totalCount
    )
}

fun UserDto.toEntity(): UserEntity{
    return UserEntity(
        id = id,
        profileImage = profileImage,
        nickName = nickName,
        introduction = introduction,
        createdAt = createdAt,
        unregisterAt = unregisterAt,
        followingCount = followingCount,
        followerCount = followerCount,
        email = email,
        isBlocked = isBlocked
    )
}

fun UserDtoWithSupabase.toEntity(): UserEntity{
    return UserEntity(
        id = id,
        profileImage = profileImage,
        nickName = nickName,
        introduction = introduction,
        createdAt = createdAt,
        unregisterAt = unregisterAt,
        followingCount = followingCount,
        followerCount = followerCount,
        email = email,
        isBlocked = isBlocked
    )
}
