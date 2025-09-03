package com.m430.plugins.app_updater;

import com.getcapacitor.Logger;

public class AppUpdaterPlugin {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
