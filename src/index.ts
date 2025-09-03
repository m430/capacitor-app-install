import { registerPlugin } from '@capacitor/core';

import type { AppUpdaterPluginPlugin } from './definitions';

const AppUpdaterPlugin = registerPlugin<AppUpdaterPluginPlugin>('AppUpdaterPlugin', {
  web: () => import('./web').then((m) => new m.AppUpdaterPluginWeb()),
});

export * from './definitions';
export { AppUpdaterPlugin };
