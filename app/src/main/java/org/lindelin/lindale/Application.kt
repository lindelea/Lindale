package org.lindelin.lindale

import android.annotation.SuppressLint
import android.content.Context
import com.github.kittinunf.fuel.core.FuelManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class Application : android.app.Application() {

    private lateinit var clientUrl: String
    private lateinit var clientId: String
    private lateinit var clientSecret: String

    fun getClientUrl() = clientUrl
    fun getClientId() = clientId
    fun getClientSecret() = clientSecret

    companion object {
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun init() {
        println("App init start...")
        Preferences(this@Application).getString(Keys.CLIENT_URL)?.let {
            FuelManager.instance.basePath = it
        }
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("system/oauth")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.value.let {
                    val oauthInfo = it as HashMap<*,*>
                    clientUrl = oauthInfo["client_url"].toString()
                    clientId = oauthInfo["client_id"].toString()
                    clientSecret = oauthInfo["client_secret"].toString()
                    Preferences(this@Application).transaction {
                        putString(Keys.CLIENT_URL, oauthInfo["client_url"].toString())
                        putString(Keys.CLIENT_ID, oauthInfo["client_id"].toString())
                        putString(Keys.CLIENT_SECRET, oauthInfo["client_secret"].toString())
                    }
                    FuelManager.instance.basePath = clientUrl
                    println("App init end...")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }
        })
    }
}