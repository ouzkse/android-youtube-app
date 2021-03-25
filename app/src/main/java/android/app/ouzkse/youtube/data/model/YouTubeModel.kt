package android.app.ouzkse.youtube.data.model

data class YouTubeModel(
    val items: List<Item>,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)