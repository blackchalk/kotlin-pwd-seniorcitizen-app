package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class LoginRequest(

	@Json(name="birthday")
	val birthday: String? = null,

	@Json(name="lastName")
	val lastName: String? = null,

	@Json(name="address")
	val address: String? = null,

	@Json(name="sex")
	val sex: String? = null,

	@Json(name="seniorImage")
	val seniorImage: String? = null,

	@Json(name="idNumber")
	val idNumber: String? = null,

	@Json(name="creationDate")
	val creationDate: String? = null,

	@Json(name="isActive")
	val isActive: Boolean? = null,

	@Json(name="isPWD")
	val isPWD: Boolean? = null,

	@Json(name="seniorCitizenID")
	val seniorCitizenID: Int? = null,

	@Json(name="firstName")
	val firstName: String? = null,

	@Json(name="password")
	val password: String? = null,

	@Json(name="isSenior")
	val isSenior: Boolean? = null,

	@Json(name="modifiedDate")
	val modifiedDate: String? = null,

	@Json(name="middleName")
	val middleName: String? = null,

	@Json(name="birthdayStr")
	val birthdayStr: Any? = null,

	@Json(name="username")
	val username: String? = null
)