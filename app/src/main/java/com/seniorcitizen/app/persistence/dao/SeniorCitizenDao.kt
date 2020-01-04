package com.seniorcitizen.app.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seniorcitizen.app.data.model.Entity
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Nic Evans on 2019-12-10.
 */
@Dao
interface SeniorCitizenDao {

    @Query("Select * from seniorCitizen where username = :username AND password = :password LIMIT 1")
    fun getSeniorCitizen(username : String, password : String) : Observable<List<Entity.SeniorCitizen>>

    @Query("Select * from seniorCitizen where username = :username LIMIT 1")
    fun attemptLoginWithUserName(username: String): Observable<List<Entity.SeniorCitizen>>

    @Query("Select * from seniorCitizen")
    fun getAllSeniorCitizen(): Single<List<Entity.SeniorCitizen>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeniorCitizen(senior : Entity.SeniorCitizen)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSeniorCitizens(seniors : List<Entity.SeniorCitizen>)

    @Query("SELECT * from seniorCitizen where idNumber like :idNumber limit 1")
    fun getSeniorCitizenByIdNumber(idNumber: String): Observable<List<Entity.SeniorCitizen>>

    @Query("DELETE FROM seniorCitizen")
    fun purgeUsers()

}