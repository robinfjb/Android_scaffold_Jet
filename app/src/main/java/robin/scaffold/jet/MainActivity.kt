package robin.scaffold.jet

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var activityViewModel: ActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_root_ide_package_.com.wayz.jetdemo.R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navView: NavigationView = findViewById(_root_ide_package_.com.wayz.jetdemo.R.id.nav_view)
        val navController = findNavController(_root_ide_package_.com.wayz.jetdemo.R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_home,
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_bluetooth,
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_wifi,
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_tools,
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_share,
                _root_ide_package_.com.wayz.jetdemo.R.id.nav_send
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        activityViewModel = ViewModelProviders.of(this)
            .get(ActivityViewModel::class.java)

        checkPermission()

        fab.setOnClickListener { view ->
           /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            activityViewModel.fabClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(_root_ide_package_.com.wayz.jetdemo.R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(_root_ide_package_.com.wayz.jetdemo.R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private fun checkPermission() {
        info("checkPermission ")

        if (!hasLocationPermission()) {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(_root_ide_package_.com.wayz.jetdemo.R.string.location_permission),
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    private fun Context.hasLocationPermission(): Boolean = EasyPermissions.hasPermissions(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_PHONE_STATE
    )

    companion object {
        const val RC_LOCATION_PERM = 101
    }
}
