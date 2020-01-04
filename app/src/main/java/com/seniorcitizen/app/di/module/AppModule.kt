package com.seniorcitizen.app.di.module

import android.app.Application
import androidx.room.Room
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao
import com.seniorcitizen.app.persistence.local.Database
import com.seniorcitizen.app.utils.Constants
import com.seniorcitizen.app.utils.Utils
import com.seniorcitizen.app.utils.Validator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Nic Evans on 2019-12-09.
 */

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : Database = Room
        .databaseBuilder(app, Database::class.java, Constants.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun provideSeniorCitizenDao(database : Database) : SeniorCitizenDao = database.seniorCitizenDao()

    @Provides
    @Singleton
    fun provideUtils(app: Application): Utils = Utils(app)

    @Provides
    @Singleton
    fun provideValidator() : Validator = Validator()

}
