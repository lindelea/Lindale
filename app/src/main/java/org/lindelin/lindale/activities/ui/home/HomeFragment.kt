package org.lindelin.lindale.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.supports.setImageFromUrl

class HomeFragment : Fragment() {

    private var profile: Profile? = null

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        loadData {
            root.profileImageView.setImageFromUrl(profile?.photo)
            root.projectCountText.text = profile?.status?.projectCount.toString()
            root.taskCountText.text = profile?.status?.unfinishedTaskCount.toString()
            root.todoCountText.text = profile?.status?.unfinishedTodoCount.toString()
        }

        return root
    }

    fun loadData(callback: () -> Unit = {}) {
        activity?.let {
            Profile.feach(it) { profile ->
                profile?.let {
                    this.profile = it
                    callback()
                }
            }
        }
    }
}