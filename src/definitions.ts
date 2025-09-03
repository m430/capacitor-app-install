export interface AppUpdaterPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
