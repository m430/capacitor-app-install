import { registerPlugin } from '@capacitor/core';

import type { AppInstallPlugin } from './definitions';

/**
 * Capacitor App Install Plugin
 *
 * ⚠️ Platform Support: This plugin only supports Android.
 * Web platform registration is required by Capacitor architecture but will throw errors.
 */
const AppInstall = registerPlugin<AppInstallPlugin>('AppInstallPlugin', {
  web: () => import('./web').then((m) => new m.AppInstallPluginWeb()),
});

export * from './definitions';
export { AppInstall as AppInstallPlugin };
