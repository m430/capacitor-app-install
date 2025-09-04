package com.m430.plugins.app_install;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.io.File;

@CapacitorPlugin(
    name = "AppInstallPlugin",
    permissions = {
        @Permission(
            strings = { Manifest.permission.READ_EXTERNAL_STORAGE },
            alias = "storage"
        )
    }
)
public class AppInstallPlugin extends Plugin {

    @Override
    public void load() {
        // Plugin initialization
    }

    @PluginMethod
    public void canInstallUnknownApps(PluginCall call) {
        try {
            boolean canInstall = canInstallUnknownAppsInternal();
            JSObject ret = new JSObject();
            ret.put("granted", canInstall);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Error checking install permission: " + e.getMessage());
        }
    }

    @PluginMethod
    public void openInstallUnknownAppsSettings(PluginCall call) {
        try {
            openInstallUnknownAppsSettingsInternal();
            call.resolve();
        } catch (Exception e) {
            call.reject("Error opening settings: " + e.getMessage());
        }
    }

    @PluginMethod
    public void hasFilePermission(PluginCall call) {
        try {
            boolean hasPermission = hasFilePermissionInternal();
            JSObject ret = new JSObject();
            ret.put("granted", hasPermission);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Error checking file permission: " + e.getMessage());
        }
    }

    @PluginMethod
    public void requestFilePermission(PluginCall call) {
        if (hasFilePermissionInternal()) {
            JSObject ret = new JSObject();
            ret.put("granted", true);
            call.resolve(ret);
        } else {
            requestPermissionForAlias("storage", call, "filePermissionCallback");
        }
    }

    @PermissionCallback
    private void filePermissionCallback(PluginCall call) {
        boolean granted = hasFilePermissionInternal();
        JSObject ret = new JSObject();
        ret.put("granted", granted);
        call.resolve(ret);
    }

    @PluginMethod
    public void installApk(PluginCall call) {
        String filePath = call.getString("filePath");
        if (filePath == null || filePath.isEmpty()) {
            call.reject("File path is required");
            return;
        }

        try {
            installApkInternal(filePath);
            call.resolve();
        } catch (Exception e) {
            call.reject("Error installing APK: " + e.getMessage());
        }
    }

    /**
     * Check if the app can install unknown apps
     */
    private boolean canInstallUnknownAppsInternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getContext().getPackageManager().canRequestPackageInstalls();
        }
        return true; // For older versions, this permission is granted by default
    }

    /**
     * Open the settings page for installing unknown apps
     */
    private void openInstallUnknownAppsSettingsInternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } else {
            // For older versions, open security settings
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }

    /**
     * Check if the app has file read permission
     */
    private boolean hasFilePermissionInternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // For older versions, this permission is granted by default
    }

    /**
     * Install an APK file
     */
    private void installApkInternal(String filePath) throws Exception {
        File apkFile = new File(filePath);
        if (!apkFile.exists()) {
            throw new Exception("APK file does not exist: " + filePath);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri apkUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Use FileProvider for Android 7.0+
            apkUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", apkFile);
        } else {
            apkUri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        getContext().startActivity(intent);
        
        Logger.info("AppInstallPlugin", "Installing APK: " + filePath);
    }
}
