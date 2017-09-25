package model.crumb

import com.google.gson.annotations.SerializedName

data class Crumb constructor(
        @SerializedName("_class") val clazz: String,
        @SerializedName("crumb") val crumb: String,
        @SerializedName("crumbRequestField") val crumbRequestField: String
)