package org.lindelin.lindale.activities.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.lindelin.lindale.models.Profile

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val profile: MutableLiveData<Profile> by lazy {
        MutableLiveData<Profile>().also {
            loadData()
        }
    }

    fun getProfile(): LiveData<Profile> {
        return profile
    }

    private fun loadData() {
        Profile.fetch(getApplication()) { profile ->
            profile?.let {
                print("000000000000000000000000000000000000000000000000000")
                this.profile.value = profile
            }
        }
    }
}