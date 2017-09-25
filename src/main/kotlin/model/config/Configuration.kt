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
        @SerializedName("urlBase") val urlBase: String,
        @SerializedName("urlPath") val urlPath: String
) {

    private var configsMap: MutableMap<String, String> = mutableMapOf()

    fun setConfigs() {
        configsMap = mutableMapOf()
        configsMap.put("titleText", titleText)
        configsMap.put("titleTextSize", titleTextSize)
        configsMap.put("titleTextColor", titleTextColor)
        configsMap.put("imageUrl", imageUrl)
        configsMap.put("imageWidth", imageWidth)
        configsMap.put("imageHeight", imageHeight)
        configsMap.put("imageScaleType", imageScaleType)
        configsMap.put("copyText", copyText)
        configsMap.put("copyTextSize", copyTextSize)
        configsMap.put("copyTextColor", copyTextColor)
        configsMap.put("sendButtonText", sendButtonText)
        configsMap.put("sendButtonTextColor", sendButtonTextColor)
        configsMap.put("sendButtonBackgroundColor", sendButtonBackgroundColor)
        configsMap.put("phoneInputTextColor", phoneInputTextColor)
        configsMap.put("phoneInputBackgroundColor", phoneInputBackgroundColor)
        configsMap.put("numPadTextColor", numPadTextColor)
        configsMap.put("numPadBackgroundColor", numPadBackgroundColor)
        configsMap.put("backgroundColor", backgroundColor)
        configsMap.put("pin", pin)
        configsMap.put("message", message)
        configsMap.put("urlBase", urlBase)
        configsMap.put("urlPath", urlPath)
    }

    fun configs(): MutableMap<String, String> {
        return configsMap
    }

}