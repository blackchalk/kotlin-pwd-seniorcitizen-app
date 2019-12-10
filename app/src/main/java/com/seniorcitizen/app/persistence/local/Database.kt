package com.seniorcitizen.app.persistence.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seniorcitizen.app.data.model.SeniorCitizen
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao

/**
 * Created by Nic Evans on 2019-12-10.
 */
@Database(entities = [SeniorCitizen::class],version = 1)
abstract class Database : RoomDatabase() {
    abstract fun seniorCitizenDao() : SeniorCitizenDao
}