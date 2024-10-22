package com.akansha.catapp.di

import android.content.Context
import androidx.room.Room
import com.akansha.catapp.data.CatRepository
import com.akansha.catapp.data.DefaultCatRepository
import com.akansha.catapp.data.source.local.CatDao
import com.akansha.catapp.data.source.local.CatDatabase
import com.akansha.catapp.data.source.network.NetworkDataSource
import com.akansha.catapp.data.source.network.RetrofitNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindCatRepository(repository: DefaultCatRepository): CatRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: RetrofitNetworkDataSource): NetworkDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): CatDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CatDatabase::class.java,
            "Cats.db"
        ).build()
    }

    @Provides
    fun provideCatDao(database: CatDatabase): CatDao = database.catDao()
}