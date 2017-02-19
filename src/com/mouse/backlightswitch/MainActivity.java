package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String PATH = "/sys/class/leds/wled:backlight/brightness";
	private String brightnessLevelToBeRestored;
	private boolean backlightIsCurrentlyOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		String brightnessLevel = getCurrentBrightnessLevel();
		isBacklightCurrentlyOff(brightnessLevel);
		setSwitchButtonText();
	}

	private String getCurrentBrightnessLevel() {
		File f = new File(PATH);
		String brightnessLevel = "";
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			sb.append(br.readLine());
			br.close();
			brightnessLevel = sb.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return brightnessLevel;
	}

	private void isBacklightCurrentlyOff(String brightnessLevel) {
		String zeroBrightness = "0";
		backlightIsCurrentlyOff = brightnessLevel.equals(zeroBrightness);
		if (backlightIsCurrentlyOff == false)
			getBrightnessLevelToBeRestored(brightnessLevel);
	}

private String getBrightnessLevelToBeRestored(String brightnessLevel) {
		brightnessLevelToBeRestored = brightnessLevel;
		return brightnessLevelToBeRestored;
	}

	private void setSwitchButtonText() {
		Button switchButton = (Button) findViewById(R.id.switch_button);
		if (backlightIsCurrentlyOff) {
			switchButton.setText(R.string.button_switch_on);
		} else {
			switchButton.setText(R.string.button_switch_off);
		}
	}

	public void switchBacklight(View view) {
		Button switchButton = (Button) findViewById(R.id.switch_button);
		if (backlightIsCurrentlyOff) {
			Toast.makeText(getApplicationContext(), R.string.message_on, Toast.LENGTH_SHORT).show();
			switchButton.setText(R.string.button_switch_off);
			switchBacklightOn();
} else {
Toast.makeText(getApplicationContext(), R.string.message_off, Toast.LENGTH_SHORT).show();
			switchButton.setText("restore" + brightnessLevelToBeRestored);
			//switchButton.setText(R.string.button_switch_on);
			switchBacklightOff();
		}
	}

public void switchBacklightOn() {
}

	public void switchBacklightOff() {
}
}
