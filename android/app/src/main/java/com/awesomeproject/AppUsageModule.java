package com.yourapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Calendar;
import java.util.List;

public class AppUsageModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    AppUsageModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

    @Override
    public String getName() {
        return "AppUsage";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @ReactMethod
    public void getUsageStats(Promise promise) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) reactContext.getSystemService(Context.USAGE_STATS_SERVICE);

        if (usageStatsManager == null) {
            promise.reject("Error", "UsageStatsManager is not available");
            return;
        }

        long endTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList == null || usageStatsList.isEmpty()) {
            promise.reject("Error", "No usage data available");
            return;
        }

        promise.resolve(usageStatsList.toString());
    }

    @ReactMethod
    public void checkForPermission(Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasUsageStatsPermission()) {
                promise.reject("Permission Error", "Permission not granted");
            } else {
                promise.resolve("Permission granted");
            }
        } else {
            promise.reject("Error", "Not supported on this version of Android");
        }
    }

    private boolean hasUsageStatsPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || Settings.Secure.getInt(reactContext.getContentResolver(), "usage_stats", 0) == 1;
    }
}
