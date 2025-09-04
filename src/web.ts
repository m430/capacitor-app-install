import { WebPlugin } from '@capacitor/core';

import type { AppInstallPlugin, InstallApkOptions, PermissionResult } from './definitions';

export class AppInstallPluginWeb extends WebPlugin implements AppInstallPlugin {
  async canInstallUnknownApps(): Promise<PermissionResult> {
    throw this.unimplemented('Not supported on web platform. This plugin only supports Android.');
  }

  async openInstallUnknownAppsSettings(): Promise<void> {
    throw this.unimplemented('Not supported on web platform. This plugin only supports Android.');
  }

  async hasFilePermission(): Promise<PermissionResult> {
    throw this.unimplemented('Not supported on web platform. This plugin only supports Android.');
  }

  async requestFilePermission(): Promise<PermissionResult> {
    throw this.unimplemented('Not supported on web platform. This plugin only supports Android.');
  }

  async installApk(_options: InstallApkOptions): Promise<void> {
    throw this.unimplemented('Not supported on web platform. This plugin only supports Android.');
  }
}
