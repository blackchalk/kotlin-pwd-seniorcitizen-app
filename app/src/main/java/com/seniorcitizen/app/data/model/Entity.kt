package com.seniorcitizen.app.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

sealed class Entity: BaseObservable(){

@Entity(
	tableName = "seniorCitizen",indices = [Index(value = ["seniorCitizenID"], unique = true)]
)
data class SeniorCitizen(

	@PrimaryKey(autoGenerate = true)
	var id : Long = 0,

	@Json(name="birthday")
	@ColumnInfo
	@Bindable
	var birthday: String? = null,

	@Json(name="lastName")
	@ColumnInfo
	@Bindable
	var lastName: String? = null,

	@Json(name="address")
	@ColumnInfo
	@Bindable
	var address: String? = null,

	@Json(name="sex")
	@ColumnInfo
	@Bindable
	var sex: String? = null,

	@Json(name="seniorImage")
	@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
	var seniorImage: ByteArray? = null,

	@Json(name="idNumber")
	@ColumnInfo
	@Bindable
	var idNumber: String? = null,

	@Json(name="creationDate")
	@ColumnInfo
	var creationDate: String? = null,

	@Json(name="isActive")
	@ColumnInfo
	val isActive: Boolean? = null,

	@Json(name="isPWD")
	@ColumnInfo
	@Bindable
	var isPWD: Boolean? = null,

	@Json(name="seniorCitizenID")
	@ColumnInfo
	@Bindable
	var seniorCitizenID: Int? = null,

	@Json(name="firstName")
	@ColumnInfo
	@Bindable
	var firstName: String? = null,

	@Json(name="password")
	@ColumnInfo
	@Bindable
	var password: String? = null,

	@Json(name="isSenior")
	@ColumnInfo
	@Bindable
	var isSenior: Boolean? = null,

	@Json(name="modifiedDate")
	@ColumnInfo
	var modifiedDate: String? = null,

	@Json(name="middleName")
	@ColumnInfo
	@Bindable
	var middleName: String? = null,

	@Json(name="username")
	@ColumnInfo
	@Bindable
	var username: String? = null
): Serializable, com.seniorcitizen.app.data.model.Entity() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SeniorCitizen

		if (seniorImage != null) {
			if (other.seniorImage == null) return false
			if (!seniorImage!!.contentEquals(other.seniorImage!!)) return false
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
}