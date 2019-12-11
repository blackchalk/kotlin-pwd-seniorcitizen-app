package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class Business(

	@Json(name="address")
	val address: String? = null,

	@Json(name="businessName")
	val businessName: String? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="type")
	val type: String? = null
)