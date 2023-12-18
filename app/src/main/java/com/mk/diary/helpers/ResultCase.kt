package com.mk.diary.helpers

sealed class ResultCase<out T> {
    data class Success<out T>(val data: T) : ResultCase<T>()
    data class Error(val message: String) : ResultCase<Nothing>()
    object Loading : ResultCase<Nothing>()
}