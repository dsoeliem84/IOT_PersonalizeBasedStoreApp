package com.iotproject.personalizedstoreapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region

class BeaconScanActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private lateinit var tvBeaconInfo: TextView
    private lateinit var tvBeaconCount: TextView
    private lateinit var tvLocationInfo: TextView
    private lateinit var tvClosestBeacon: TextView
    private lateinit var btnViewProducts: Button
    private lateinit var btnViewPromos: Button

    private var detectedBeacons = mutableMapOf<String, Beacon>()
    private var closestBeacon: Beacon? = null

    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon_scan)

        tvBeaconInfo = findViewById(R.id.tvBeaconInfo)
        tvBeaconCount = findViewById(R.id.tvBeaconCount)
        tvLocationInfo = findViewById(R.id.tvLocationInfo)
        tvClosestBeacon = findViewById(R.id.tvClosestBeacon)
        btnViewProducts = findViewById(R.id.btnViewProducts)
        btnViewPromos = findViewById(R.id.btnViewPromos)

        btnViewProducts.isEnabled = false
        btnViewPromos.isEnabled = false

        btnViewProducts.setOnClickListener { openProductList("all") }
        btnViewPromos.setOnClickListener { openProductList("promo") }

        beaconManager = BeaconManager.getInstanceForApplication(this)

        // ✅ IMPORTANT: Tell AltBeacon what beacon format to detect (iBeacon)
        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")

        )

        checkPermissions()
    }

    private fun openProductList(mode: String) {
        val beacon = closestBeacon
        if (beacon == null) {
            Toast.makeText(this, "No beacon selected", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ProductListActivity::class.java).apply {
            putExtra("UUID", beacon.id1.toString())
            putExtra("MAJOR", beacon.id2.toString()) // floor
            putExtra("MINOR", beacon.id3.toString()) // aisle_id
            putExtra("MODE", mode)
        }
        startActivity(intent)
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            startScanning()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startScanning()
            } else {
                Toast.makeText(
                    this,
                    "Permissions are required to scan beacons",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startScanning() {
        beaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        val region = Region("all-beacons", null, null, null)

        // ✅ Always update UI, even if 0 beacons are found
        beaconManager.addRangeNotifier(RangeNotifier { beacons, _ ->
            runOnUiThread { updateBeaconInfo(beacons) }
        })

        try {
            beaconManager.startRangingBeacons(region)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateBeaconInfo(beacons: Collection<Beacon>) {
        if (beacons.isEmpty()) {
            tvBeaconInfo.text = "Waiting for beacons..."
            tvBeaconCount.text = "Found 0 beacon(s)"
            tvClosestBeacon.text = "No beacons detected"
            tvClosestBeacon.setTextColor(
                ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            )
            btnViewProducts.isEnabled = false
            btnViewPromos.isEnabled = false
            closestBeacon = null
            return
        }

        // Update detected beacons map
        beacons.forEach { beacon ->
            val beaconId = "${beacon.id1}-${beacon.id2}-${beacon.id3}"
            detectedBeacons[beaconId] = beacon
        }

        // Find closest beacon
        closestBeacon = beacons.minByOrNull { it.distance }

        val sb = StringBuilder()
        sb.append("Detected Beacons:\n\n")

        val sortedBeacons = beacons.sortedBy { it.distance }
        sortedBeacons.forEach { beacon ->
            val isClosest = beacon == closestBeacon

            if (isClosest) sb.append(">>> CLOSEST <<<\n")

            sb.append("UUID: ${beacon.id1}\n")
            sb.append("Major (floor): ${beacon.id2}\n")
            sb.append("Minor (aisle): ${beacon.id3}\n")
            sb.append("Distance: ${"%.2f".format(beacon.distance)}m\n")
            sb.append("RSSI: ${beacon.rssi}\n")

            if (isClosest) sb.append(">>> CLOSEST <<<\n")

            sb.append("---\n\n")
        }

        tvBeaconInfo.text = sb.toString()
        tvBeaconCount.text = "Found ${detectedBeacons.size} unique beacon(s)"

        closestBeacon?.let { beacon ->
            tvClosestBeacon.text =
                "📍 Closest: ${beacon.id1}-${beacon.id2}-${beacon.id3} (${"%.2f".format(beacon.distance)}m)"
            tvClosestBeacon.setTextColor(
                ContextCompat.getColor(this, android.R.color.holo_green_dark)
            )
            btnViewProducts.isEnabled = true
            btnViewPromos.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }
}
