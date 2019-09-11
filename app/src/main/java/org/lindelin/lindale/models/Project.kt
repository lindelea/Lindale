package org.lindelin.lindale.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

data class Project(var id: Int,
                   var title: String,
                   var content: String,
                   var start_at: String?,
                   var end_at: String?,
                   var image: String,
                   var type: String) {

    class Deserializer : ResponseDeserializable<Project> {
        override fun deserialize(content: String): Project {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, Project::class.java)
        }
    }
}