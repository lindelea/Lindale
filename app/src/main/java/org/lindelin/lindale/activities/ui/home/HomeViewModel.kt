package org.lindelin.lindale.activities.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import org.lindelin.lindale.models.MyTaskCollection
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val profile: MutableLiveData<Profile> by lazy {
        MutableLiveData<Profile>().also {
            loadProfileData()
        }
    }

    private val myTaskCollection: MutableLiveData<MyTaskCollection> by lazy {
        MutableLiveData<MyTaskCollection>().also {
            loadMyTaskData()
        }
    }

    private val userPhoto = MutableLiveData<Bitmap>()

    fun getProfile(): LiveData<Profile> {
        return profile
    }

    fun getMyTasks(): LiveData<MyTaskCollection> {
        return myTaskCollection
    }

    fun getUserPhoto(): LiveData<Bitmap> {
        return userPhoto
    }

    fun refreshData(callBack: () -> Unit) {
        Profile.fetch(getApplication()) { profile ->
            profile?.let {
                this.profile.postValue(it)
                loadImage(it.photo)
                syncPreferences(profile)
                callBack()
            }
        }
    }

    private fun loadProfileData() {
        Profile.fetch(getApplication()) { profile ->
            profile?.let {
                this.profile.value = it
                loadImage(it.photo)
                syncPreferences(profile)
            }
        }
    }

    private fun loadMyTaskData() {
        MyTaskCollection.fetch(getApplication()) {
            it?.let {
                this.myTaskCollection.value = it
            }
        }
    }

    private fun loadImage(url: String) {
        val httpAsync = url.httpGet().response { result ->

            val (data, _) = result

            data?.let {
                this.userPhoto.value = BitmapFactory.decodeByteArray(it, 0, it.size)
            }
        }

        httpAsync.join()
    }

    private fun syncPreferences(profile: Profile) {
        Preferences(getApplication()).transaction {
            putString(Keys.USER_NAME, profile.name)
            putString(Keys.USER_EMAIL, profile.email)
            putString(Keys.USER_CONTENT, profile.content)
            putString(Keys.USER_ORG, profile.company)
        }
    }
}