package com.mouse.backlightswitch;

import android.app.Application;
import android.content.Intent;

public class BacklightSwitchApplication extends Application {

	public static final String START_ACTION = "com.mouse.backlightswitch.start";

	@Override
	public void onCreate() {
		super.onCreate();
		startService(new Intent(START_ACTION, null, this, BacklightSwitchService.class));
	}

}
