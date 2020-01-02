package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class UserTransactionRequest(

	@Json(name="SeniorCitizenID")
	val SeniorCitizenID: Int? = null
)