package khvatid.wallpaper.domain.models

data class ImageModel(
    val id: String = "",
    val urls: Urls = Urls(),
) {
    data class Urls(
        val small: String = "",
        val full: String = "",
        val raw: String = ""
    )
}