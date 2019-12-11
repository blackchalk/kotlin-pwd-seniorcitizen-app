package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class AppAuthenticateResponse(

	@Json(name="password")
	val password: Any? = null,

	@Json(name="expiration")
	val expiration: String? = null,

	@Json(name="username")
	val username: String? = null,

	@Json(name="token")
	val token: String? = null
)