package com.example.localdatabaseproject.models.reels

import com.fasterxml.jackson.annotation.JsonProperty

data class ReelsResult(
    @JsonProperty("data")
    val data: Data,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("message")
    val message: String? = null
)

data class Data(
    @JsonProperty("items")
    val items: List<Item>,
    @JsonProperty("paging_info")
    val pagingInfo: PagingInfo,
    @JsonProperty("status")
    val status: String
)

data class Item(
    @JsonProperty("media")
    val media: Media
)

data class Media(
    @JsonProperty("device_timestamp")
    val deviceTimestamp: Long,
    @JsonProperty("caption")
    val caption: Caption,
    @JsonProperty("play_count")
    val playCount: Int,
    @JsonProperty("fb_play_count")
    val fbPlayCount: Int,
    @JsonProperty("like_count")
    val likeCount: Int,
    @JsonProperty("number_of_qualities")
    val numberOfQualities: Int,
    @JsonProperty("video_versions")
    val videoVersions: List<VideoVersion>,
    @JsonProperty("video_duration")
    val videoDuration: Double
)

data class Caption(
    @JsonProperty("created_at")
    val createdAt: Long,
    @JsonProperty("text")
    val text: String,
    @JsonProperty("user")
    val user: User
)

data class User(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("full_name")
    val fullName: String,
    @JsonProperty("username")
    val username: String,
    @JsonProperty("profile_pic_url")
    val profilePicUrl: String
)

data class VideoVersion(
    @JsonProperty("height")
    val height: Int,
    @JsonProperty("type")
    val type: Int,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("width")
    val width: Int
)

data class PagingInfo(
    @JsonProperty("max_id")
    val maxId: String,
    @JsonProperty("more_available")
    val moreAvailable: Boolean
)

