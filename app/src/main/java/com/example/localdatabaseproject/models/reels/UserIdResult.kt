package com.example.localdatabaseproject.models.reels

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

data class UserIdResult(
    @JsonProperty("data")
    val data: UserID?=null,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("message")
    val message: String?
)

data class UserID(
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.AS_EMPTY) // Treat empty string as null
    var id: String? = null
)


