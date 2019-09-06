package org.lindelin.lindale.models

import android.content.Context
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpPost
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.lindelin.lindale.Application
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

data class OAuth(var tokenType: String,
                 var expiresIn: Int,
                 var accessToken: String,
                 var refreshToken: String) {

    companion object {
        fun login(context: Context, email: String, password: String, callback: (Boolean) -> Unit) {
            val app = context.applicationContext as Application
            val httpAsync = "/oauth/token"
                .httpPost(listOf(
                    "grant_type" to "password",
                    "client_id" to app.getClientId(),
                    "client_secret" to app.getClientSecret(),
                    "username" to email,
                    "password" to password,
                    "scope" to "*"
                ))
                .responseObject(OAuth.Deserializer()) {
                        _, _, result ->
                    val (oauth, error) = result

                    error?.let {
                        callback(false)
                        println(it.response.statusCode)
                        return@responseObject
                    }

                    oauth?.save(context)

                    callback(true)
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