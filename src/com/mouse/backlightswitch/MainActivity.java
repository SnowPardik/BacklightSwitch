package com.mouse.backlightswitch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	public static final String START_ACTION = "com.mouse.backlightswitch.start";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(START_ACTION, null, this, BacklightSwitchService.class));
		finish();
	}

}
