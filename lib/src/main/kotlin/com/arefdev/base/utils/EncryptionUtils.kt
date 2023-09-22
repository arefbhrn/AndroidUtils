package com.arefdev.base.utils

import android.util.Base64
import org.lsposed.lsparanoid.Obfuscate
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
@Obfuscate
object EncryptionUtils {

    private const val ALGORITHM = "AES/CTR/NoPadding"
    private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val SECRET_KEY_SPEC_ALGORITHM = "AES"
    private const val KEY = "sdc09sd8csm123pq"
    private val KEY_BYTES = KEY.toByteArray()
    private val SALT_BYTES = "custom_encryption".toByteArray()
    private const val BUF_LENGTH = 8192

    private var encryptCipher: Cipher? = null
        get() = try {
            if (field == null) {
                field = Cipher.getInstance(ALGORITHM)
                val iv = ByteArray(16)
                val ivSpec = IvParameterSpec(iv)
                val secretKey = SecretKeySpec(KEY_BYTES, SECRET_KEY_SPEC_ALGORITHM)
                field!!.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            }
            field
        } catch (e: Exception) {
            null
        }
    private var decryptCipher: Cipher? = null
        get() = try {
            if (field == null) {
                field = Cipher.getInstance(ALGORITHM)
                val iv = ByteArray(16)
                val ivSpec = IvParameterSpec(iv)
                val secretKey = SecretKeySpec(KEY_BYTES, SECRET_KEY_SPEC_ALGORITHM)
                field!!.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            }
            field
        } catch (e: Exception) {
            null
        }

    fun encrypt(src: File): Boolean {
        return if (encryptCipher == null) false else try {
            val dst = File(src.path + ".enc")
            val inputStream: InputStream = FileInputStream(src)
            val os: OutputStream = CipherOutputStream(FileOutputStream(dst), encryptCipher)
            val buf = ByteArray(BUF_LENGTH)
            while (true) {
                val n = inputStream.read(buf)
                if (n == -1)
                    break
                os.write(buf, 0, n)
            }
            os.close()
            inputStream.close()
            dst.renameTo(src)
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    fun decrypt(src: File): Boolean {
        return if (decryptCipher == null) false else try {
            val dst = File(src.path + ".dec")
            val buf = ByteArray(BUF_LENGTH)
            val inputStream: InputStream = FileInputStream(src)
            val os: OutputStream = CipherOutputStream(FileOutputStream(src), decryptCipher)
            while (true) {
                val n = inputStream.read(buf)
                val offset = 0
                if (n == -1) break
                os.write(buf, offset, n)
            }
            os.close()
            inputStream.close()
            dst.renameTo(src)
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    fun encrypt(strToEncrypt: String): String? {
        return try {
            val iv = ByteArray(16)
            val ivSpec = IvParameterSpec(iv)
            val factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
            val spec: KeySpec = PBEKeySpec(
                KEY.toCharArray(), SALT_BYTES, Math.pow(2.0, iv.size.toDouble())
                    .toInt(), 256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(tmp.encoded, SECRET_KEY_SPEC_ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decrypt(strToDecrypt: String): String? {
        return try {
            val iv = ByteArray(16)
            val ivSpec = IvParameterSpec(iv)
            val factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
            val spec: KeySpec = PBEKeySpec(
                KEY.toCharArray(), SALT_BYTES, Math.pow(2.0, iv.size.toDouble())
                    .toInt(), 256
            )
            val tmp = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(tmp.encoded, SECRET_KEY_SPEC_ALGORITHM)
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isEncrypted(file: File): Boolean {
        return try {
            val buf = ByteArray(BUF_LENGTH)
            val c = Cipher.getInstance(ALGORITHM)
            c.init(Cipher.DECRYPT_MODE, SecretKeySpec(KEY_BYTES, SECRET_KEY_SPEC_ALGORITHM), IvParameterSpec(ByteArray(16)))
            val inputStream: InputStream = FileInputStream(file)
            val n = inputStream.read(buf)
            if (n == -1) return false
            inputStream.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
