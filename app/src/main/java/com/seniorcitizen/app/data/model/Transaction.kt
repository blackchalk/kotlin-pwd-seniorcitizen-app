package com.seniorcitizen.app.data.model

import com.squareup.moshi.Json

data class Transaction(

	@Json(name="seniorCitizen")
	val seniorCitizen: Entity.SeniorCitizen? = null,

	@Json(name="totalQuantity")
	val totalQuantity: Int? = null,

	@Json(name="business")
	val business: Business? = null,

	@Json(name="modifiedDate")
	val modifiedDate: String? = null,

	@Json(name="orNumber")
	val orNumber: String? = null,

	@Json(name="transactionDate")
	val transactionDate: String? = null,

	@Json(name="creationDate")
	val creationDate: String? = null,

	@Json(name="isActive")
	val isActive: Boolean? = null,

	@Json(name="transactionID")
	val transactionID: Int? = null,

	@Json(name="transactionDetail")
	val transactionDetail: List<TransactionDetailItem?>? = null
)