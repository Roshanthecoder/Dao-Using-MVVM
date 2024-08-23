package com.example.localdatabaseproject.models.exercises

import com.fasterxml.jackson.annotation.JsonProperty

data class ExerciseResult(
    @JsonProperty("bodyPart") val bodyPart: String?,
    @JsonProperty("equipment") val equipment: String?,
    @JsonProperty("gifUrl") val gifUrl: String?,
    @JsonProperty("id") val id: String?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("target") val target: String?,
    @JsonProperty("secondaryMuscles") val secondaryMuscles: List<String>?,
    @JsonProperty("instructions") val instructions: List<String>?
)