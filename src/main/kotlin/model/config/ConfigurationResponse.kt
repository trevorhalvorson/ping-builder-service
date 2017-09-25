package model.config

import com.google.gson.annotations.SerializedName

data class ConfigurationResponse constructor(
        @SerializedName("success") val success: Boolean,
        @SerializedName("message") val message: String
)