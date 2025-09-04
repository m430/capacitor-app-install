/**
 * Capacitor App Install Plugin Definitions
 * 
 * ⚠️ Platform Support: This plugin only supports Android platform.
 * iOS and Web platforms are not supported.
 */

export interface InstallApkOptions {
  /**
   * The file path of the APK to install
   */
  filePath: string;
}

export interface PermissionResult {
  /**
   * Whether the permission is granted
   */
  granted: boolean;
}

export interface InstallApkResult {
  /**
   * Whether the installation was completed successfully
   */
  completed: boolean;
  /**
   * Message describing the installation result
   */
  message: string;
}

export interface AppInstallPlugin {
  /**
   * Check if the app can install unknown apps (install from unknown sources)
   * @returns Promise<PermissionResult>
   */
  canInstallUnknownApps(): Promise<PermissionResult>;

  /**
   * Open the settings page for installing unknown apps
   * @returns Promise<void>
   */
  openInstallUnknownAppsSettings(): Promise<void>;

  /**
   * Install an APK file
   * @param options - The options containing the file path
   * @returns Promise<InstallApkResult>
   */
  installApk(options: InstallApkOptions): Promise<InstallApkResult>;
}
