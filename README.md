# @m430/capacitor-app-install

**Android Only** - A Capacitor plugin for APK installation and permission management on Android devices. This plugin provides essential functionality for app installation, including permission checks and APK installation capabilities.

> ⚠️ **Platform Support**: This plugin only supports Android. iOS and Web platforms are not supported.

## Features

- ✅ Check install unknown apps permission
- ✅ Open install unknown apps settings
- ✅ Check file read permission
- ✅ Request file read permission
- ✅ Install APK files
- ✅ Android only (Web and iOS platforms not supported)
- ✅ TypeScript support

## Install

```bash
npm install @m430/capacitor-app-install
npx cap sync
```

## Android Configuration

### 1. Add FileProvider to AndroidManifest.xml

Add the following to your `android/app/src/main/AndroidManifest.xml` inside the `<application>` tag:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### 2. Create file_paths.xml

Create `android/app/src/main/res/xml/file_paths.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="." />
    <external-cache-path name="external_cache" path="." />
    <files-path name="files" path="." />
    <cache-path name="cache" path="." />
</paths>
```

### 3. Add Permissions

Add the following permissions to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

## Usage

```typescript
import { AppInstallPlugin } from '@m430/capacitor-app-install';

// Check if app can install unknown apps
const { granted } = await AppInstallPlugin.canInstallUnknownApps();
if (!granted) {
  // Open settings to allow install from unknown sources
  await AppInstallPlugin.openInstallUnknownAppsSettings();
}

// Check file permission
const filePermission = await AppInstallPlugin.hasFilePermission();
if (!filePermission.granted) {
  // Request file permission
  const result = await AppInstallPlugin.requestFilePermission();
  if (!result.granted) {
    console.log('File permission denied');
    return;
  }
}

// Install APK
try {
  await AppInstallPlugin.installApk({
    filePath: '/path/to/your/app.apk'
  });
  console.log('APK installation started');
} catch (error) {
  console.error('Failed to install APK:', error);
}
```

## API

<docgen-index>

* [`canInstallUnknownApps()`](#caninstallunknownapps)
* [`openInstallUnknownAppsSettings()`](#openinstallunknownappssettings)
* [`hasFilePermission()`](#hasfilepermission)
* [`requestFilePermission()`](#requestfilepermission)
* [`installApk(...)`](#installapk)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### canInstallUnknownApps()

```typescript
canInstallUnknownApps() => Promise<PermissionResult>
```

Check if the app can install unknown apps (install from unknown sources)

**Returns:** <code>Promise&lt;<a href="#permissionresult">PermissionResult</a>&gt;</code>

--------------------


### openInstallUnknownAppsSettings()

```typescript
openInstallUnknownAppsSettings() => Promise<void>
```

Open the settings page for installing unknown apps

--------------------


### hasFilePermission()

```typescript
hasFilePermission() => Promise<PermissionResult>
```

Check if the app has file read permission

**Returns:** <code>Promise&lt;<a href="#permissionresult">PermissionResult</a>&gt;</code>

--------------------


### requestFilePermission()

```typescript
requestFilePermission() => Promise<PermissionResult>
```

Request file read permission

**Returns:** <code>Promise&lt;<a href="#permissionresult">PermissionResult</a>&gt;</code>

--------------------


### installApk(...)

```typescript
installApk(options: InstallApkOptions) => Promise<void>
```

Install an APK file

| Param         | Type                                                            | Description                            |
| ------------- | --------------------------------------------------------------- | -------------------------------------- |
| **`options`** | <code><a href="#installapkoptions">InstallApkOptions</a></code> | - The options containing the file path |

--------------------


### Interfaces


#### PermissionResult

| Prop          | Type                 | Description                       |
| ------------- | -------------------- | --------------------------------- |
| **`granted`** | <code>boolean</code> | Whether the permission is granted |


#### InstallApkOptions

Capacitor App Install Plugin Definitions

⚠️ Platform Support: This plugin only supports Android platform.
iOS and Web platforms are not supported.

| Prop           | Type                | Description                         |
| -------------- | ------------------- | ----------------------------------- |
| **`filePath`** | <code>string</code> | The file path of the APK to install |

</docgen-api>

## Platform Support

| Platform | Supported |
| -------- | --------- |
| Android  | ✅        |
| iOS      | ❌        |
| Web      | ❌        |

## Error Handling

The plugin throws errors in the following cases:

- **File not found**: When the specified APK file doesn't exist
- **Permission denied**: When required permissions are not granted
- **Platform not supported**: When called on non-Android platforms

## License

MIT

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute to this project.
