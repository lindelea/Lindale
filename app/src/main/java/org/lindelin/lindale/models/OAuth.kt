package org.lindelin.lindale.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpPost
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

data class OAuth(var tokenType: String,
                 var expiresIn: Int,
                 var accessToken: String,
                 var refreshToken: String) {

    companion object {
        fun login(email: String, password: String, callback: (OAuth?) -> Unit) {
            val httpAsync = "https://lindale.stg.lindelin.org/oauth/token"
                .httpPost(listOf(
                    "grant_type" to "password",
                    "client_id" to "1",
                    "client_secret" to "QYvbbd9zoXWtOEBYTVhspVnGoPMUgEckD95Lfmjv",
                    "username" to email,
                    "password" to password,
                    "scope" to "*"
                ))
                .responseObject(OAuth.Deserializer()) {
                        request, response, result ->
                    val (oauth, error) = result

                    error?.let {
                        callback(null)
                        println(it.response.statusCode)
                        return@responseObject
                    }

                    println(oauth!!.accessToken)
                    callback(oauth)
                }
            httpAsync.join()
        }
    }

    class Deserializer : ResponseDeserializable<OAuth> {
        override fun deserialize(content: String): OAuth {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, OAuth::class.java)
        }
    }
}