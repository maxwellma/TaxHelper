package com.maxwell.mclib.encode

import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {

    fun getMd5LowerCase(string: String?): String? {
        val hash: ByteArray
        if (null == string) {
            return null
        }
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            hex.append(hexString)
        }

        return hex.toString()
    }

    fun getMd5LowerCase(bytes: ByteArray?): String? {
        val hash: ByteArray
        if (null == bytes) {
            return null
        }
        try {
            hash = MessageDigest.getInstance("MD5").digest(bytes)
        } catch (e: Exception) {
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            hex.append(hexString)
        }

        return hex.toString()
    }

    fun getMd5UpperCase(string: String?): String? {
        val hash: ByteArray
        if (null == string) {
            return null
        }
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            hex.append(hexString)
        }

        return hex.toString().toUpperCase()
    }

    fun getMd5UpperCase(bytes: ByteArray?): String? {
        val hash: ByteArray
        if (null == bytes) {
            return null
        }
        try {
            hash = MessageDigest.getInstance("MD5").digest(bytes)
        } catch (e: Exception) {
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var i :Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            hex.append(hexString)
        }

        return hex.toString().toUpperCase()
    }
}

