package com.seniorcitizen.app.data.model

import javax.annotation.Generated
import com.squareup.moshi.Json

@Generated("com.robohorse.robopojogenerator")
data class AppAuthenticateRequest(

	@Json(name="password")
	val password: String? = null,

	@Json(name="username")
	val username: String? = null
)