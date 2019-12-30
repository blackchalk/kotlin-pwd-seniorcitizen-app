package com.seniorcitizen.app.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seniorcitizen.app.data.model.SeniorCitizen
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Nic Evans on 2019-12-10.
 */
@Dao
interface SeniorCitizenDao {

    @Query("Select * from seniorCitizen where username = :username AND password = :password LIMIT 1")
    fun getSeniorCitizen(username : String, password : String) : Observable<List<SeniorCitizen>>

    @Query("Select * from seniorCitizen")
    fun getAllSeniorCitizen(): Single<List<SeniorCitizen>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeniorCitizen(senior : SeniorCitizen)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSeniorCitizens(seniors : List<SeniorCitizen>)

    @Query("SELECT * from seniorCitizen where idNumber like :idNumber limit 1")
    fun getSeniorCitizenByIdNumber(idNumber: String): Observable<List<SeniorCitizen>>

}