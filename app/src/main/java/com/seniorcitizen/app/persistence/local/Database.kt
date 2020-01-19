package com.seniorcitizen.app.persistence.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao

/**
 * Created by Nic Evans on 2019-12-10.
 */
@Database(entities = [Entity.SeniorCitizen::class],version = 3)
abstract class Database : RoomDatabase() {
    abstract fun seniorCitizenDao() : SeniorCitizenDao
}