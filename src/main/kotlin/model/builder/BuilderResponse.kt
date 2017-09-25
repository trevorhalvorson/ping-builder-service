package model.builder

import com.google.gson.annotations.SerializedName

data class BuilderResponse constructor(
        @SerializedName("success") val success: Boolean,
        @SerializedName("response") val response: String
)