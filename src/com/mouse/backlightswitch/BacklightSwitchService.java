package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BacklightSwitchService extends Service {
	private static final int NOTIFICATION_ID = 1;
	private static final String NOTIFICATION_ACTION = "com.mouse.backlightswitch.notification";
	private static final String PATH = "/sys/class/leds/wled:backlight/brightness";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotification();
		onNotificationClick(intent.getAction());
		return super.onStartCommand(intent, flags, startId);
	}

	private void createNotification() {
		CharSequence t = getResources().getString(R.string.switch_off);
		Context c = getApplicationContext();
		Intent i = new Intent(NOTIFICATION_ACTION, null, c, BacklightSwitchService.class);
		PendingIntent pi = PendingIntent.getService(c, 0, i, 0);
		Notification n = new Notification.Builder(c).setSmallIcon(R.drawable.ic_launcher).setContentText(t)
				.setContentIntent(pi).setShowWhen(false).setPriority(Notification.PRIORITY_MIN).build();
		startForeground(NOTIFICATION_ID, n);
	}

	private void onNotificationClick(String intentAction) {
		if (intentAction.equals(NOTIFICATION_ACTION)) {
			Toast.makeText(getApplicationContext(), "Получаю интент" + intentAction, Toast.LENGTH_SHORT).show();
			defineSwitchOperation();
		}
	}

	private void defineSwitchOperation() {
		String brightnessLevel = getCurrentBrightnessLevel();
		boolean backlightIsCurrentlyOff = isBacklightCurrentlyOff(brightnessLevel);
		String brightnessLevelToBeRestored = getBrightnessLevelToBeRestored(backlightIsCurrentlyOff, brightnessLevel);
		switchBacklight(backlightIsCurrentlyOff, brightnessLevelToBeRestored);
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

	private boolean isBacklightCurrentlyOff(String brightnessLevel) {
		String zeroBrightness = "0";
		boolean backlightIsCurrentlyOff = brightnessLevel.equals(zeroBrightness);
		return backlightIsCurrentlyOff;
	}

	private String getBrightnessLevelToBeRestored(boolean backlightIsCurrentlyOff, String brightnessLevel) {
		String brightnessLevelToBeRestored = "";
		if (backlightIsCurrentlyOff == false) {
			brightnessLevelToBeRestored = brightnessLevel;
		}
		return brightnessLevelToBeRestored;
	}

	private void switchBacklight(boolean backlightIsCurrentlyOff, String brightnessLevelToBeRestored) {
		if (backlightIsCurrentlyOff) {
			switchBacklightOn(brightnessLevelToBeRestored);
			String notificationText = getResources().getString(R.string.switch_off);
			updateNotification(notificationText);
		} else {
			switchBacklightOff();
			String notificationText = getResources().getString(R.string.switch_on);
			updateNotification(notificationText);
		}
	}

	private void switchBacklightOn(String brightnessLevelToBeRestored) {
	}

	private void switchBacklightOff() {
	}

	private void updateNotification(String notificationText) {
		Intent i = new Intent(NOTIFICATION_ACTION, null, this, BacklightSwitchService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		Notification n = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher)
				.setContentText(notificationText).setContentIntent(pi).setShowWhen(false)
				.setPriority(Notification.PRIORITY_MIN).build();
		startForeground(NOTIFICATION_ID, n);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
