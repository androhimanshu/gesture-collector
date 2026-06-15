package com.touchtrace.app.util;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import com.touchtrace.app.model.SessionMeta;
import java.util.UUID;

/** Captures device/session metadata for normalization. */
public final class DeviceInfo {
    public static SessionMeta capture(Activity a) {
        DisplayMetrics dm = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        SessionMeta m = new SessionMeta();
        m.sessionId = UUID.randomUUID().toString();
        m.deviceModel = Build.MODEL;
        m.manufacturer = Build.MANUFACTURER;
        m.androidVersion = Build.VERSION.RELEASE;
        m.sdkInt = Build.VERSION.SDK_INT;
        m.screenWidthPx = dm.widthPixels;
        m.screenHeightPx = dm.heightPixels;
        m.density = dm.density;
        m.xdpi = dm.xdpi; m.ydpi = dm.ydpi;
        m.refreshRateHz = a.getWindowManager().getDefaultDisplay().getRefreshRate();
        m.startedEpochMs = System.currentTimeMillis();
        return m;
    }
}
