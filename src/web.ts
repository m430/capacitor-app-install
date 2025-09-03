import { WebPlugin } from '@capacitor/core';

import type { AppUpdaterPluginPlugin } from './definitions';

export class AppUpdaterPluginWeb extends WebPlugin implements AppUpdaterPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
