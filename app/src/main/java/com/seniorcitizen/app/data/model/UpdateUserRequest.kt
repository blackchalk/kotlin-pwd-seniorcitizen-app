package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class UpdateUserRequest(

	@Json(name="Address")
	var Address: String? = null,

	@Json(name="Username")
	var Username: String? = null,

	@Json(name="FirstName")
	var FirstName: String? = null,

	@Json(name="Sex")
	var Sex: String? = null,

	@Json(name="IsPWD")
	var IsPWD: Boolean? = null,

	@Json(name="Birthday")
	var Birthday: String? = null,

	@Json(name="LastName")
	var LastName: String? = null,

	@Json(name="MiddleName")
	var MiddleName: String? = null,

	@Json(name="IsSenior")
	var IsSenior: Boolean? = null,

	@Json(name="IDNumber")
	var IDNumber: String? = null,

	@Json(name="Password")
	var Password: String? = null,

	@Json(name="SeniorCitizenID")
	var SeniorCitizenID: Int? = null,

	@Json(name="SeniorImage")
	var SeniorImage: String? = null
)