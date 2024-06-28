package com.awesomeproject

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

class MainApplication : Application(), ReactApplication {

    private val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(this) {
            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    // Ajoutez votre package personnalisé ici si nécessaire
                }

            override fun getJSMainModuleName(): String = "index"

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
        }

    private val reactHost: ReactHost
        get() = getDefaultReactHost(applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // Si vous avez activé la Nouvelle Architecture, chargez le point d'entrée natif pour cette application.
            load()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkUsageStatsPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkUsageStatsPermission() {
        val usageStatsManager =
            getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
                ?: return

        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 * 60 // 1 hour ago

        val stats =
            usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )

        if (stats != null) {
            // Traitement des statistiques d'utilisation ici
            for (stat in stats) {
                // Logique pour traiter chaque stat
                // Vous pouvez par exemple imprimer les informations des applications
                println("Package Name: ${stat.packageName}, Total Time Used: ${stat.totalTimeInForeground} ms")
            }
        } else {
            println("Stats is null")
        }
    }
}
