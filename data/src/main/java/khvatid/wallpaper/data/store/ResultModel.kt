package khvatid.wallpaper.data.store

sealed class ResultModel<out T>{

    data class Success<out T>(val data: T?): ResultModel<T>()
    data class Failure(val code: Int,val message : String): ResultModel<Nothing>()

}