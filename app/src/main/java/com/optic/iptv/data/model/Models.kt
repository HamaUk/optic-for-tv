package com.optic.iptv.data.model

data class Category(
    val id: String = "",
    val name: String = "",
    val count: Int = 0
)

data class Channel(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val logo: String = "",
    val categoryId: String = "",
    val resolution: String = "1920x1080",
    val codec: String = "H.264",
    val bitrate: String = "5.0 Mbps",
    val fps: Int = 50
)
