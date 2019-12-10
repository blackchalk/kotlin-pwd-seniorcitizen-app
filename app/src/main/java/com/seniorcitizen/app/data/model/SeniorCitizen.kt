package com.seniorcitizen.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
	tableName = "seniorCitizen"
)
data class SeniorCitizen(

	@PrimaryKey(autoGenerate = true)
	var id : Long = 0,

	@Json(name="birthday")
	@ColumnInfo
	val birthday: String? = null,

	@Json(name="lastName")
	@ColumnInfo
	val lastName: String? = null,

	@Json(name="address")
	@ColumnInfo
	val address: String? = null,

	@Json(name="sex")
	@ColumnInfo
	val sex: String? = null,

	@Json(name="seniorImage")
	@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
	val seniorImage: ByteArray? = null,

	@Json(name="idNumber")
	@ColumnInfo
	val idNumber: String? = null,

	@Json(name="creationDate")
	@ColumnInfo
	val creationDate: String? = null,

	@Json(name="isActive")
	@ColumnInfo
	val isActive: Boolean? = null,

	@Json(name="isPWD")
	@ColumnInfo
	val isPWD: Boolean? = null,

	@Json(name="seniorCitizenID")
	@ColumnInfo
	val seniorCitizenID: Int? = null,

	@Json(name="firstName")
	@ColumnInfo
	val firstName: String? = null,

	@Json(name="password")
	@ColumnInfo
	val password: String? = null,

	@Json(name="isSenior")
	@ColumnInfo
	val isSenior: Boolean? = null,

	@Json(name="modifiedDate")
	@ColumnInfo
	val modifiedDate: String? = null,

	@Json(name="middleName")
	@ColumnInfo
	val middleName: String? = null,

	@Json(name="username")
	@ColumnInfo
	val username: String? = null
): Serializable {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SeniorCitizen

		if (seniorImage != null) {
			if (other.seniorImage == null) return false
			if (!seniorImage.contentEquals(other.seniorImage)) return false
		} else if (other.seniorImage != null) return false
		if (isSenior != other.isSenior) return false
		if (modifiedDate != other.modifiedDate) return false
		if (middleName != other.middleName) return false
		if (username != other.username) return false

		return true
	}

	override fun hashCode(): Int {
		return seniorImage?.contentHashCode() ?: 0
	}
}