package com.m430.plugins.app_install;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.io.File;

@CapacitorPlugin(name = "AppInstallPlugin", permissions = {
        @Permission(strings = { Manifest.permission.READ_EXTERNAL_STORAGE }, alias = "storage")
})
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
    public void installApk(PluginCall call) {
        try {
            String filePath = call.getString("filePath");
            if (filePath == null || filePath.isEmpty()) {
                call.reject("File path is required");
                return;
            }

            // 处理file:// URI格式的路径
            String actualFilePath = filePath;
            if (filePath.startsWith("file://")) {
                actualFilePath = Uri.parse(filePath).getPath();
            }

            // 检查文件是否存在
            File apkFile = new File(actualFilePath);
            if (!apkFile.exists()) {
                call.reject("APK file does not exist: " + actualFilePath);
                return;
            }

            // 检查安装权限
            if (!canInstallUnknownAppsInternal()) {
                call.reject("Install unknown apps permission is required");
                return;
            }

            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Android 7.0+ 使用 FileProvider
                Uri apkUri = FileProvider.getUriForFile(
                        getContext(),
                        getContext().getPackageName() + ".fileprovider",
                        apkFile);
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                // Android 7.0 以下使用 file:// URI
                Uri apkUri = Uri.fromFile(apkFile);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            getActivity().startActivity(intent);

            JSObject ret = new JSObject();
            ret.put("completed", true);
            ret.put("message", "APK installation started");
            call.resolve(ret);

        } catch (Exception e) {
            call.reject("Failed to install APK", e);
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

}
