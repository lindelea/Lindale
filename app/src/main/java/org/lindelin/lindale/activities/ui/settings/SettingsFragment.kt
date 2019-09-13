package org.lindelin.lindale.activities.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import org.lindelin.lindale.R
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.models.Settings
import org.lindelin.lindale.supports.Keys
import org.lindelin.lindale.supports.Preferences

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            Keys.USER_NOTIFICATION_SLACK -> updateNotificationConfig()
            Keys.USER_LANG -> updateLangConfig()
            else -> updateProfile()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun updateNotificationConfig() {
        activity?.let {
            Settings.Notification.sync(it)
        }
    }

    private fun updateLangConfig() {
        activity?.let {
            Settings.Locale.sync(it)
        }
    }

    private fun updateProfile() {
        activity?.let {
            Profile.sync(it)
        }
    }
}
