package org.lindelin.lindale.models

import android.content.Context
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpPost
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

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
                        _, _, result ->
                    val (oauth, error) = result

                    error?.let {
                        callback(null)
                        println(it.response.statusCode)
                        return@responseObject
                    }

                    callback(oauth)
                }
            httpAsync.join()
        }
    }

    fun save(context: Context) {
        Preferences(context).transaction {
            putString(Keys.ACCESS_TOKEN, accessToken)
            putString(Keys.REFRESH_TOKEN, refreshToken)
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