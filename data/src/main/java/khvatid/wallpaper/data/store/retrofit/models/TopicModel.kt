package khvatid.wallpaper.data.store.retrofit.models


data class TopicModel(
    val id: String = "",
    val slug: String = "",
    val title: String = "",
    val description: String = "",
    val links: Url = Url(),
    val cover_photo: PhotoModel = PhotoModel()
) {

    data class Url(
        val self: String = "",
        val photos: String = ""
    )

    data class Owner(
        val id: String = "",
        val name: String = ""
    )

}
