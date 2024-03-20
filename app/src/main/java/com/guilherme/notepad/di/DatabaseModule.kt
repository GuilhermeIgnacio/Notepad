package com.guilherme.notepad.di

import com.guilherme.notepad.data.MongoRepository
import com.guilherme.notepad.data.MongoRepositoryImpl
import com.guilherme.notepad.models.Note
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(Note::class)
        )
            .compactOnLaunch()
            .build()

        return Realm.open(config)

    }

    @Singleton
    @Provides
    fun provideMongoRepository(realm: Realm): MongoRepository{
        return MongoRepositoryImpl(realm = realm)
    }
}