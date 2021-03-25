package android.app.ouzkse.youtube.data

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}