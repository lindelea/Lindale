package org.lindelin.lindale.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.supports.setImageFromUrl

class HomeFragment : Fragment() {

    private var profile: Profile? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData {
            profileImageView.setImageFromUrl(profile?.photo)
            projectCountText.text = profile?.status?.projectCount.toString()
            taskCountText.text = profile?.status?.unfinishedTaskCount.toString()
            todoCountText.text = profile?.status?.unfinishedTodoCount.toString()
        }
    }

    fun loadData(callback: () -> Unit = {}) {
        Profile.fetch(this.context!!) { profile ->
            profile?.let {
                this.profile = it
                callback()
            }
        }
    }
}