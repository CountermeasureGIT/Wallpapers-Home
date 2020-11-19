package ru.countermeasure.wallpapershome.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.countermeasure.wallpapershome.data.local.ImageCacheDataSourceImpl
import ru.countermeasure.wallpapershome.data.network.ImageRemoteDataSourceImpl
import ru.countermeasure.wallpapershome.domain.abstractions.ImageCacheDataSource
import ru.countermeasure.wallpapershome.domain.abstractions.ImageRemoteDataSource

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindImageCacheDataSource(
        imageCacheDataSource: ImageCacheDataSourceImpl
    ): ImageCacheDataSource

    @Binds
    abstract fun bindImageRemoteDataSource(
        imageRemoteDataSourceImpl: ImageRemoteDataSourceImpl
    ): ImageRemoteDataSource
}