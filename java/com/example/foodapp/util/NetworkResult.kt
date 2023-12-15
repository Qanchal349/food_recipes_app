package com.example.foodapp.util

sealed class NetworkResult<T>(
    val data : T?=null,  //  data will be any type(means any model FoodRecipe or Result)
    val message:String?=null //  but message is always String so not required Generic for it like<T,G>
) {

    class Success<T>(data: T):NetworkResult<T>(data)
    class Error<T>(message: String?,data:T?=null):NetworkResult<T>(data,message)
    class Loading<T>:NetworkResult<T>()


}