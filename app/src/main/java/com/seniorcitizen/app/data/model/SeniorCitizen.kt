package com.seniorcitizen.app.data.model

import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
	tableName = "seniorCitizen"
)
data class SeniorCitizen(

	@Json(name="birthday")
	@ColumnInfo
	@Bindable
	val birthday: String? = null,

	@Json(name="lastName")
	@ColumnInfo
	@Bindable
	val lastName: String? = null,

	@Json(name="address")
	@ColumnInfo
	@Bindable
	val address: String? = null,

	@Json(name="sex")
	@ColumnInfo
	@Bindable
	val sex: String? = null,

	@Json(name="seniorImage")
	@ColumnInfo
	@Bindable
	val seniorImage: Any? = null,

	@Json(name="idNumber")
	@ColumnInfo
	@Bindable
	val idNumber: String? = null,

	@Json(name="creationDate")
	@ColumnInfo
	val creationDate: String? = null,

	@Json(name="isActive")
	@ColumnInfo
	val isActive: Boolean? = null,

	@Json(name="isPWD")
	@ColumnInfo
	@Bindable
	val isPWD: Boolean? = null,

	@Json(name="seniorCitizenID")
	@ColumnInfo
	@Bindable
	val seniorCitizenID: Int? = null,

	@Json(name="firstName")
	@ColumnInfo
	@Bindable
	val firstName: String? = null,

	@Json(name="password")
	@ColumnInfo
	@Bindable
	val password: String? = null,

	@Json(name="isSenior")
	@ColumnInfo
	@Bindable
	val isSenior: Boolean? = null,

	@Json(name="modifiedDate")
	@ColumnInfo
	val modifiedDate: String? = null,

	@Json(name="middleName")
	@ColumnInfo
	@Bindable
	val middleName: String? = null,

	@Json(name="username")
	@ColumnInfo
	@Bindable
	val username: String? = null
): Serializable