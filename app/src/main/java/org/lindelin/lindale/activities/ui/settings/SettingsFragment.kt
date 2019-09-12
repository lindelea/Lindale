package org.lindelin.lindale.activities.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import org.lindelin.lindale.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
