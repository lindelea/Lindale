package org.lindelin.lindale.models

import android.content.Context
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

data class MyTaskCollection(var data: MutableList<Task>) {

    companion object {
        fun fetch(context: Context, callback: (MyTaskCollection?) -> Unit) {
            Preferences(context).getString(Keys.ACCESS_TOKEN)?.let { accessToken ->
                "/api/tasks"
                    .httpGet()
                    .authentication()
                    .bearer(accessToken)
                    .appendHeader("Accept", "application/json")
                    .responseObject(MyTaskCollection.Deserializer()) {
                            _, _, result ->
                        val (myTaskCollection, error) = result

                        error?.let {
                            callback(null)
                            println(it.response.statusCode)
                            return@responseObject
                        }

                        callback(myTaskCollection)
                    }
            }
        }
    }

    class Deserializer : ResponseDeserializable<MyTaskCollection> {
        override fun deserialize(content: String): MyTaskCollection {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, MyTaskCollection::class.java)
        }
    }
}

data class Task(var projectName: String,
                var id: Int,
                var initiator: User?,
                var title: String,
                var content: String?,
                var start_at: String?,
                var end_at: String?,
                var cost: Int,
                var progress: Int,
                var user: User?,
                var color: Int,
                var type: String,
                var status: String,
                var subTaskStatus: String,
                var group: String?,
                var priority: String,
                var isFinish: Int,
                var updatedAt: String) {

    class Deserializer : ResponseDeserializable<Task> {
        override fun deserialize(content: String): Task {
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            return gson.fromJson(content, Task::class.java)
        }
    }
}