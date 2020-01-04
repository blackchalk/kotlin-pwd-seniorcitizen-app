package com.seniorcitizen.app.utils

import java.io.IOException
import java.nio.charset.Charset
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.UnrecoverableEntryException
import java.security.cert.CertificateException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by Nic Evans on 2020-01-04.
 */

/**
 * _____        _____                  _
 * |  __ \      / ____|                | |
 * | |  | | ___| |     _ __ _   _ _ __ | |_ ___  _ __
 * | |  | |/ _ \ |    | '__| | | | '_ \| __/ _ \| '__|
 * | |__| |  __/ |____| |  | |_| | |_) | || (_) | |
 * |_____/ \___|\_____|_|   \__, | .__/ \__\___/|_|
 * __/ | |
 * |___/|_|
 */


/**
 * Credits to https://gist.github.com/JosiasSena/3bf4ca59777f7dedcaf41a495d96d984
 */
class DeCryptor {

    private var keyStore: KeyStore? = null
    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class
    )
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore!!.load(null)
    }

    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class
    )
    fun decryptData(
        alias: String,
        encryptedData: ByteArray?,
        encryptionIv: ByteArray?
    ): String {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        return String(cipher.doFinal(encryptedData), Charset.forName("UTF-8"))
    }

    @Throws(
        NoSuchAlgorithmException::class,
        UnrecoverableEntryException::class,
        KeyStoreException::class
    )
    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).getSecretKey()
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    }

    init {
        initKeyStore()
    }
}