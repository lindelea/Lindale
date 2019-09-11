package org.lindelin.lindale.models

import android.content.Context
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.lindelin.lindale.Application
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

data class Profile(var id: Int,
                   var name: String,
                   var email: String,
                   var photo: String,
                   var content: String?,
                   var company: String?,
                   var location: String?,
                   var createdAt: String,
                   var updatedAt: String,
                   var status: Status,
                   var progress: Progress,
                   var activity: String,
                   var projects: Projects) {

    companion object {
        fun fetch(context: Context, callback: (Profile?) -> Unit) {
            Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                "/api/profile"
                    .httpGet()
                    .authentication()
                    .bearer(accessToken)
                    .appendHeader("Accept", "application/json")
                    .responseObject(Profile.Deserializer()) {
                            _, _, result ->
                        val (profile, error) = result

                        error?.let {
                            callback(null)
                            println(it.response.statusCode)
                            return@responseObject
                        }

                        callback(profile)
                    }
            }
        }
    }

    data class Status(var projectCount: Int,
                      var unfinishedTaskCount: Int,
                      var unfinishedTodoCount: Int) {

        class Deserializer : ResponseDeserializable<Status> {
            override fun deserialize(content: String): Status {
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                return gson.fromJson(content, Status::class.java)
            }
        }
    }

    data class Progress(var total: Int,
                        var task: Int,
                        var todo: Int) {

        class Deserializer : ResponseDeserializable<Progress> {
            override fun deserialize(content: String): Progress {
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                return gson.fromJson(content, Progress::class.java)
            }
        }
    }

    data class Projects(var favorites: MutableList<Project>) {

        class Deserializer : ResponseDeserializable<Projects> {
            override fun deserialize(content: String): Projects {
                val gson = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                return gson.fromJson(content, Projects::class.java)
            }
        }
    }

    class Deserializer : ResponseDeserializable<Profile> {
        override fun deserialize(content: String): Profile {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, Profile::class.java)
        }
    }
}