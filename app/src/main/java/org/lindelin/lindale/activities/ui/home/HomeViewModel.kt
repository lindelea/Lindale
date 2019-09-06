package org.lindelin.lindale.activities.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.lindelin.lindale.models.Profile

class HomeViewModel : ViewModel() {
    val _profile = MutableLiveData<Profile>()

    fun set(item: Profile) {
        _profile.value = item
    }

    val profile: LiveData<Profile> = _profile
}