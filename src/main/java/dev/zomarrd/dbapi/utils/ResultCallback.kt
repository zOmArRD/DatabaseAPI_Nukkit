/*
 * Created by IntelliJ Idea
 *
 * User: zOmArRD
 * Date: 12/9/2023
 *
 * Copyright (c) 2023. OMY TECHNOLOGY by <dev@zomarrd.me>
 */

package dev.zomarrd.dbapi.utils

data class ResultOrError<T>(
    val data: T? = null,
    val error: Exception? = null,
)

open class ResultCallback<T> {
    var onComplete: ((ResultOrError<T>) -> Unit)? = null

    open fun success(value: T) {
        onComplete?.invoke(ResultOrError(data = value))
    }

    open fun error(exception: Exception) {
        onComplete?.invoke(ResultOrError(error = exception))
    }
}