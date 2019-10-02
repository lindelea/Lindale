package org.lindelin.lindale.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

data class User(var id: Int,
                var name: String,
                var email: String,
                var content: String?,
                var company: String?,
                var location: String?,
                var photo: String,
                var createdAt: String,
                var updatedAt: String) {

    class Deserializer : ResponseDeserializable<User> {
        override fun deserialize(content: String): User {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, User::class.java)
        }
    }
}