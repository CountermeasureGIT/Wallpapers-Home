package ru.countermeasure.wallpapershome.domain.models

sealed class OperationStatus {
    data class InProgress(
        val progress: Int
    ) : OperationStatus()

    data class Finished<T>(
        val result: T
    ) : OperationStatus()
}