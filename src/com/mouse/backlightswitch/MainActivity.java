package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String PATH = "/sys/class/leds/wled:backlight/brightness";
	private String brightnessLevelToBeRestored;
	private boolean backlightIsCurrentlyOff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void switchBacklight(View view) {
		if (backlightIsCurrentlyOff) {
			switchBacklightOn();
			defineSwitchButton();
		} else {
startService();
			switchBacklightOff();
			defineSwitchButton();
		}
	}

	public void switchBacklightOn() {
	}

	public void startService() {
		String startAction = "start";
		startService(new Intent(startAction,null, this, BacklightSwitchService.class));
	}

	public void switchBacklightOff() {
	}

	@Override
	protected void onStart() {
		super.onStart();
		defineSwitchButton();
	}

	private void defineSwitchButton() {
		String brightnessLevel = getCurrentBrightnessLevel();
		identifyBacklightState(brightnessLevel);
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

	private void identifyBacklightState(String brightnessLevel) {
		String zeroBrightness = "0";
		backlightIsCurrentlyOff = brightnessLevel.equals(zeroBrightness);
		if (backlightIsCurrentlyOff == false)
			setBrightnessLevelToBeRestored(brightnessLevel);
	}

	private void setBrightnessLevelToBeRestored(String brightnessLevel) {
		brightnessLevelToBeRestored = brightnessLevel;
	}

	private void setSwitchButtonText() {
		Button switchButton = (Button) findViewById(R.id.switch_button);
		if (backlightIsCurrentlyOff) {
			switchButton.setText("Restore" + brightnessLevelToBeRestored);
		} else {
			switchButton.setText(R.string.switch_off);
		}
	}
}
