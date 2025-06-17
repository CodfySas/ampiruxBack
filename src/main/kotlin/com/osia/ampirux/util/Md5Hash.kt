package com.osia.ampirux.util

import java.math.BigInteger
import java.security.MessageDigest

class Md5Hash {

    fun createMd5(password: String): String {
        val md5Crypt = MessageDigest.getInstance("MD5")
        return BigInteger(1, md5Crypt.digest(password.toByteArray())).toString(16).padStart(32, '0')
    }
}
