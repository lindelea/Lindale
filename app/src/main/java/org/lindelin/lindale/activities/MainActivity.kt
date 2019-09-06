package org.lindelin.lindale.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.lindelin.lindale.R
import org.lindelin.lindale.models.Profile
import org.lindelin.lindale.supports.setImageFromUrl

class MainActivity : AppCompatActivity() {

    var profile: Profile? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )

        loadData {
            setupSideBarHeader(navView)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    fun loadData(callback: () -> Unit = {}) {
        Profile.fetch(this) { profile ->
            profile?.let {
                this.profile = it
                callback()
            }
        }
    }

    fun setupSideBarHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)

        headerView.sideBarPhoto.setImageFromUrl(profile?.photo)

        headerView.sideBarNameText.apply {
            text = profile?.name
        }

        headerView.sideBarEmailText.apply {
            text = profile?.email
        }
    }
}
