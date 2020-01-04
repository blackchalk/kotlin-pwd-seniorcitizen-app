package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class AppAuthenticateRequest(

	@Json(name="password")
	var password: String? = "password",

	@Json(name="username")
	var username: String? = "test"
)