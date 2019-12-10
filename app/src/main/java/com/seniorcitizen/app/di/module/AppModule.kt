package com.seniorcitizen.app.di.module

import android.app.Application
import androidx.room.Room
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao
import com.seniorcitizen.app.persistence.local.Database
import com.seniorcitizen.app.utils.Constants
import com.seniorcitizen.app.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Nic Evans on 2019-12-09.
 */

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideSeniorCitizenDatabase(app: Application) : Database = Room.databaseBuilder(app,
        Database::class.java, Constants.DATABASE_NAME)
        /*.addMigrations(MIGRATION_1_2)*/
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideSeniorCitizenDao(database : Database) : SeniorCitizenDao = database.seniorCitizenDao()

    @Provides
    @Singleton
    fun provideUtils(): Utils = Utils(app)
}
