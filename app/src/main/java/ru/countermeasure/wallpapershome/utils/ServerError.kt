package ru.countermeasure.wallpapershome.utils

class ServerError(val code: String, message: String? = null) : Throwable(message)