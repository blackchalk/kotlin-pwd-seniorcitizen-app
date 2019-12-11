package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class AppAuthenticateRequest(

	@Json(name="password")
	val password: String? = null,

	@Json(name="username")
	val username: String? = null
)