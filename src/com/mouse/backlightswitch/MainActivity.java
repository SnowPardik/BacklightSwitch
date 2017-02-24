package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
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
		createNotification();
	}

public void  createNotification() {
int notification_id = 1;
	CharSequence t = "Notification";
	CharSequence s = "Here is a notification"; 
	Context c = getApplicationContext();
Notification n = new Notification.Builder(c).setContentTitle(t).setContentText(s).setSmallIcon(R.drawable.ic_launcher).build();
NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
nm.notify(notification_id, n);
}

	public void switchBacklight(View view) {
		if (backlightIsCurrentlyOff) {
			switchBacklightOn();
			defineSwitchButton();
		} else {
			switchBacklightOff();
			defineSwitchButton();
		}
	}

	public void switchBacklightOn() {
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
			switchButton.setText("Restore" + brightnessLevelToBeRestored);
		} else {
			switchButton.setText(R.string.button_switch_off);
		}
	}
}
