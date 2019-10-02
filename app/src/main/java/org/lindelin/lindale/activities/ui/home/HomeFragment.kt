package org.lindelin.lindale.activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.supports.loadHtmlString
import org.lindelin.lindale.supports.onProgressChanged

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var refreshControl: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel = activity?.run {
            ViewModelProviders.of(this)[HomeViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        homeViewModel.getProfile().observe(this, Observer {
            updateUI(it)
        })

        homeViewModel.getUserPhoto().observe(this, Observer {
            profileImageView.setImageBitmap(it)
        })

        setupRefreshControl(rootView)

        return rootView
    }

    override fun onRefresh() {
        homeViewModel.refreshData {
            refreshControl.isRefreshing = false
        }
    }

    fun updateUI(profile: Profile) {
        projectProgressBar.onProgressChanged(profile.progress.total)
        progressBar.onProgressChanged(profile.progress.task)
        todoProgressBar.onProgressChanged(profile.progress.todo)
        activityWebView.loadHtmlString(profile.activity)
        projectCountText.text = profile.status.projectCount.toString()
        taskCountText.text = profile.status.unfinishedTaskCount.toString()
        todoCountText.text = profile.status.unfinishedTodoCount.toString()
    }

    fun setupRefreshControl(view: View) {
        refreshControl = view.findViewById(R.id.swipeContainer)
        refreshControl.setOnRefreshListener(this)
        refreshControl.setColorSchemeResources(R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark)
    }
}