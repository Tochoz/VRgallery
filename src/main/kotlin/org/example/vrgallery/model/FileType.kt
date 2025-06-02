package org.example.vrgallery.model

enum class FileType {
    APK, IMAGE, VIDEO
}

fun FileType.toLocationString(name: String): String {
    return "storage/${
        when (this.name) {
        "APK" -> "apk"
        "IMAGE" -> "media"
        "VIDEO" -> "media"
        else -> ""
    }
    }/${name}"
}