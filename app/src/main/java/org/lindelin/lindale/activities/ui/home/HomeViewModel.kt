package org.lindelin.lindale.activities.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.kittinunf.fuel.httpGet
import org.lindelin.lindale.models.Profile

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val profile: MutableLiveData<Profile> by lazy {
        MutableLiveData<Profile>().also {
            loadData()
        }
    }

    private val userPhoto = MutableLiveData<Bitmap>()

    fun getProfile(): LiveData<Profile> {
        return profile
    }

    fun getUserPhoto(): LiveData<Bitmap> {
        return userPhoto
    }

    private fun loadData() {
        Profile.fetch(getApplication()) { profile ->
            profile?.let {
                this.profile.value = it
                loadImage(it.photo)
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
}