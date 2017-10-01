package model.config

import com.google.gson.annotations.SerializedName

open class Configuration(
        @SerializedName("titleText") val titleText: String,
        @SerializedName("titleTextSize") val titleTextSize: String,
        @SerializedName("titleTextColor") val titleTextColor: String,
        @SerializedName("imageUrl") val imageUrl: String,
        @SerializedName("imageWidth") val imageWidth: String,
        @SerializedName("imageHeight") val imageHeight: String,
        @SerializedName("imageScaleType") val imageScaleType: String,
        @SerializedName("copyText") val copyText: String,
        @SerializedName("copyTextSize") val copyTextSize: String,
        @SerializedName("copyTextColor") val copyTextColor: String,
        @SerializedName("sendButtonText") val sendButtonText: String,
        @SerializedName("sendButtonTextColor") val sendButtonTextColor: String,
        @SerializedName("sendButtonBackgroundColor") val sendButtonBackgroundColor: String,
        @SerializedName("phoneInputTextColor") val phoneInputTextColor: String,
        @SerializedName("phoneInputBackgroundColor") val phoneInputBackgroundColor: String,
        @SerializedName("numPadTextColor") val numPadTextColor: String,
        @SerializedName("numPadBackgroundColor") val numPadBackgroundColor: String,
        @SerializedName("backgroundColor") val backgroundColor: String,
        @SerializedName("pin") val pin: String,
        @SerializedName("message") val message: String,
        @SerializedName("messagingUrlBase") val messagingUrlBase: String,
        @SerializedName("messagingUrlPath") val messagingUrlPath: String,
        @SerializedName("builderUrlBase") val builderUrlBase: String,
        @SerializedName("builderUrlPath") val builderUrlPath: String,
        @SerializedName("email") val email: String
)
