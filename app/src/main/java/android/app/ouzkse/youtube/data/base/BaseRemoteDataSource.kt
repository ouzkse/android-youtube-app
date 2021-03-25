package android.app.ouzkse.youtube.data.base

import android.app.ouzkse.youtube.data.Result
import retrofit2.Response
import java.io.IOException

abstract class BaseRemoteDataSource {

    protected suspend fun <T> getResult(
        serviceCall: suspend () -> Response<T>
    ): Result<T> {

        try {
            val response = serviceCall()

            if (response.isSuccessful) {
                response.body().takeIf { it != null }?.let { responseBody ->
                    return Result.Success(responseBody)
                }
            }
            return Result.Failure(IOException("${response.code()} ${response.message()}"))
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }
}