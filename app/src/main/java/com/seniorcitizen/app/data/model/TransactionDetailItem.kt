package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class TransactionDetailItem(

	@Json(name="item")
	val item: String? = null,

	@Json(name="quantity")
	val quantity: Int? = null,

	@Json(name="price")
	val price: Double? = null,

	@Json(name="creationDate")
	val creationDate: String? = null
)