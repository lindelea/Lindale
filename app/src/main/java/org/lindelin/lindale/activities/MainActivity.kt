package org.lindelin.lindale.activities

import android.graphics.Bitmap
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.lindelin.lindale.R
import org.lindelin.lindale.activities.ui.favorite.FavoriteFragment
import org.lindelin.lindale.activities.ui.home.HomeViewModel
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.models.Project
import org.lindelin.lindale.supports.setImageFromUrl

class MainActivity : AppCompatActivity(), FavoriteFragment.OnListFragmentInteractionListener {

    override fun onListFragmentInteraction(item: Project?) {
        println(item)
    }

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favorite
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        homeViewModel.getProfile().observe(this, Observer<Profile>{
            setupSideBarHeader(navView, it)
        })

        homeViewModel.getUserPhoto().observe(this, Observer {
            setupSideBarHeaderImage(navView, it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setupSideBarHeader(navView: NavigationView, profile: Profile) {
        val headerView = navView.getHeaderView(0)
        headerView.sideBarPhoto.setImageFromUrl(profile.photo)
        headerView.sideBarNameText.text = profile.name
        headerView.sideBarEmailText.text = profile.email
    }

    fun setupSideBarHeaderImage(navView: NavigationView, image: Bitmap) {
        val headerView = navView.getHeaderView(0)
        headerView.sideBarPhoto.setImageBitmap(image)
    }
}
