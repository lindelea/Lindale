package org.lindelin.lindale.models

import android.content.Context
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPut
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class Settings() {
    data class Locale(var currentLanguage: String,
                      var currentLanguageName: String) {

        companion object {
            fun fetch(context: Context, callback: (Locale?) -> Unit) {
                Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                    "/api/settings/locale"
                        .httpGet()
                        .authentication()
                        .bearer(accessToken)
                        .appendHeader("Accept", "application/json")
                        .responseObject(Settings.Locale.Deserializer()) {
                                _, _, result ->
                            val (locale, error) = result

                            error?.let {
                                callback(null)
                                println(it.response.statusCode)
                                return@responseObject
                            }

                            callback(locale)
                        }
                }
            }

            fun sync(context: Context) {
                Preferences(context).getString(Keys.USER_LANG)?.let { config ->
                    Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                        "/api/settings/locale"
                            .httpPut(listOf(
                                "language" to config
                            ))
                            .authentication()
                            .bearer(accessToken)
                            .appendHeader("Accept", "application/json")
                            .response { _, _, result ->
                                val (_, error) = result

                                error?.let {
                                    println(it.response.statusCode)
                                }
                            }
                    }
                }
            }
        }

        class Deserializer : ResponseDeserializable<Locale> {
            override fun deserialize(content: String): Locale {
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                return gson.fromJson(content, Locale::class.java)
            }
        }
    }

    data class Notification(var slack: String) {

        fun getSlackConfig(): Boolean {
            return when (slack) {
                "on" -> true
                "off" -> false
                else -> false
            }
        }

        companion object {
            fun fetch(context: Context, callback: (Notification?) -> Unit) {
                Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                    "/api/settings/notification"
                        .httpGet()
                        .authentication()
                        .bearer(accessToken)
                        .appendHeader("Accept", "application/json")
                        .responseObject(Settings.Notification.Deserializer()) {
                                _, _, result ->
                            val (notification, error) = result

                            error?.let {
                                callback(null)
                                println(it.response.statusCode)
                                return@responseObject
                            }

                            callback(notification)
                        }
                }
            }

            fun sync(context: Context) {
                Preferences(context).getBoolean(Keys.USER_NOTIFICATION_SLACK)?.let { config ->
                    Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                        "/api/settings/notification"
                            .httpPut(listOf(
                                "slack" to if (config) "on" else "off"
                            ))
                            .authentication()
                            .bearer(accessToken)
                            .appendHeader("Accept", "application/json")
                            .response { _, _, result ->
                                val (_, error) = result

                                error?.let {
                                    println(it.response.statusCode)
                                }
                            }
                    }
                }
            }
        }

        class Deserializer : ResponseDeserializable<Notification> {
            override fun deserialize(content: String): Notification {
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                return gson.fromJson(content, Notification::class.java)
            }
        }
    }
}