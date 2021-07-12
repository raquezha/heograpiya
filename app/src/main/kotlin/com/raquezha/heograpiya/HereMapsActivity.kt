package com.raquezha.heograpiya

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.raquezha.heograpiya.databinding.ActivityHeremapsBinding
import java.util.Arrays

open class HereMapsActivity : AppCompatActivity() {

    private val binding: ActivityHeremapsBinding by lazy {
        ActivityHeremapsBinding.inflate(layoutInflater)
    }

    // permissions request code
    private val REQUEST_CODE_ASK_PERMISSIONS = 1

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private val REQUIRED_SDK_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private var mapFragmentView: MapFragmentView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (mapFragmentView == null) {
            false
        } else mapFragmentView!!.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mapFragmentView == null) {
            false
        } else mapFragmentView!!.onOptionsItemSelected(item)
    }

    private fun createRoute() {

    }
    private fun setupMapFragmentView() {
        setContentView(binding.root)

        binding.btnCreateRoute.setOnClickListener {
            createRoute()
        }

        // All permission requests are being handled. Create map fragment view. Please note
        // the HERE Mobile SDK requires all permissions defined above to operate properly.
        mapFragmentView = MapFragmentView(this)
        invalidateOptionsMenu()
    }

    /**
     * Checks the dynamically controlled permissions and requests missing permissions from end user.
     */
    private fun checkPermissions() {
        val missingPermissions: MutableList<String> = ArrayList()
        // check all required dynamic permissions
        for (permission in REQUIRED_SDK_PERMISSIONS) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission)
            }
        }
        if (missingPermissions.isNotEmpty()) {
            // request all missing permissions
            val permissions = missingPermissions.toTypedArray()
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            val grantResults = IntArray(REQUIRED_SDK_PERMISSIONS.size)
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
            onRequestPermissionsResult(
                REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                grantResults
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                var index = permissions.size - 1
                while (index >= 0) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(
                            this, "Required permission '" + permissions[index]
                                    + "' not granted, exiting", Toast.LENGTH_LONG
                        ).show()
                        finish()
                        return
                    }
                    --index
                }
                // all permissions were granted
                setupMapFragmentView()
            }
        }
    }
}