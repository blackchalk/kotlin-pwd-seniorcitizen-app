package com.seniorcitizen.app.utils

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * Help class used to encrypt a string.
 *
 * @author Miguel Francisco García del Moral Muñoz.
 */
object SimpleCrypto {
    @Throws(Exception::class)
    fun encrypt(seed: String, cleartext: String): String {
        val rawKey = getRawKey(seed.toByteArray())
        val result = encrypt(rawKey, cleartext.toByteArray())
        return toHex(result)
    }

    @Throws(Exception::class)
    fun decrypt(seed: String, encrypted: String): String {
        val rawKey = getRawKey(seed.toByteArray())
        val enc = toByte(encrypted)
        val result = decrypt(rawKey, enc)
        return String(result)
    }

    @Throws(Exception::class)
    private fun getRawKey(seed: ByteArray): ByteArray {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        val md5Bytes: ByteArray = md.digest(seed) // 128 Bit = 16 byte SecretKey
        val skey = SecretKeySpec(md5Bytes, "AES")
        return skey.getEncoded()
    }

    @Throws(Exception::class)
    private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    private fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher: Cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }

    fun toHex(txt: String): String {
        return toHex(txt.toByteArray())
    }

    fun fromHex(hex: String): String {
        return String(toByte(hex))
    }

    fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] =
            Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).toByte()
        return result
    }

    fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private const val HEX = "0123456789ABCDEF"
    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX.toCharArray()[b.toInt() shr 4 and 0x0f]).append(HEX.toCharArray()[b.toInt() and 0x0f])
    }

    infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)
    infix fun Int.shl(that: Byte): Int = this.shl(that.toInt()) // Not necessary in this case because no there's (Int shl Byte)
    infix fun Byte.shl(that: Byte): Int = this.toInt().shl(that.toInt()) // Not necessary in this case because no there's (Byte shl Byte)

    infix fun Byte.and(that: Int): Int = this.toInt().and(that)
    infix fun Int.and(that: Byte): Int = this.and(that.toInt()) // Not necessary in this case because no there's (Int and Byte)
    infix fun Byte.and(that: Byte): Int = this.toInt().and(that.toInt())
}