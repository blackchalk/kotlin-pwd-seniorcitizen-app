package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class UpdateUserResponse(

	@Json(name="success")
	val success: Boolean? = null,

	@Json(name="message")
	val message: String? = null,

	@Json(name="status")
	val status: Int? = null
)