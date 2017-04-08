package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class BacklightSwitchService extends Service {
	private static final int NOTIFICATION_ID = 1;
	private static final String NOTIFICATION_ACTION = "com.mouse.backlightswitch.notification";
	private static final String PATH = "/sys/class/leds/wled:backlight/brightness";

	@Override
	public void onCreate() {
		super.onCreate();
registerReceiver(new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(getApplicationContext(), "Я проснулся", Toast.LENGTH_SHORT).show(); 
	}
},
new IntentFilter(Intent.ACTION_SCREEN_ON));
}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotification();
		String intentAction = intent.getAction(); 
		if (NOTIFICATION_ACTION.equals(intentAction)) {
handleNotificationClick();
		}
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

	private void handleNotificationClick() {
			Toast.makeText(getApplicationContext(), "Получаю интент", Toast.LENGTH_SHORT).show();
			defineSwitchOperation();
			// switchBacklight();
		}

	private void defineSwitchOperation() {
		String brightnessLevel = getCurrentBrightnessLevel();
		boolean backlightIsCurrentlyOff = isBacklightCurrentlyOff(brightnessLevel);
		String brightnessLevelToBeRestored = getBrightnessLevelToBeRestored(backlightIsCurrentlyOff, brightnessLevel);
//DimmerView dimmer_view = getDimmerView();
//LayoutParams dimmer_params = defineDimmerParams();
		String notificationText = getNotificationText(backlightIsCurrentlyOff);
//switchBacklight(backlightIsCurrentlyOff, brightnessLevelToBeRestored, dimmer_view, dimmer_params, notificationText);
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

private DimmerView getDimmerView() {
	DimmerView dimmer_view = new DimmerView(this);
	return dimmer_view;
}

private LayoutParams defineDimmerParams() {
	WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	LayoutParams dimmer_params = new LayoutParams();
	dimmer_params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
	dimmer_params.flags |= LayoutParams.FLAG_DIM_BEHIND; 
	dimmer_params.dimAmount = 1.0f;
	dimmer_params.flags |= LayoutParams.FLAG_NOT_FOCUSABLE;
	dimmer_params.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
dimmer_params.flags |= LayoutParams.FLAG_FULLSCREEN;
	dimmer_params.flags &= ~LayoutParams.FLAG_KEEP_SCREEN_ON;
		dimmer_params.flags &= ~LayoutParams.FLAG_TURN_SCREEN_ON;
	dimmer_params.format = PixelFormat.OPAQUE;

	Point p = new Point();
	wm.getDefaultDisplay().getRealSize(p);
	dimmer_params.width = p.x;
	dimmer_params.height = p.y;

return dimmer_params;
}

	private String getNotificationText(boolean backlightIsCurrentlyOff) {
		String notificationText = "";
		if (backlightIsCurrentlyOff) {
			notificationText = getResources().getString(R.string.switch_off);
		} else {
			notificationText = getResources().getString(R.string.switch_on);
		}
		return notificationText;
	}

	private void switchBacklight(boolean backlightIsCurrentlyOff, String brightnessLevelToBeRestored, DimmerView dimmer_view, LayoutParams dimmer_params, String notificationText) {
		if (backlightIsCurrentlyOff) {
			switchBacklightOn(brightnessLevelToBeRestored);
//removeDimmerOutOfTheScreen(dimmer_view);
			updateNotification(notificationText);
		} else {
			switchBacklightOff();
			putDimmerOverTheScreen(dimmer_view, dimmer_params);
			updateNotification(notificationText);
		}
	}

	private void switchBacklightOn(String brightnessLevelToBeRestored) {
	}

	private void switchBacklightOff() {
	}

	private void putDimmerOverTheScreen(DimmerView dimmer_view, LayoutParams dimmer_params) {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		wm.addView(dimmer_view, dimmer_params);
}

	private void removeDimmerOutOfTheScreen(DimmerView dimmer_view) {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		wm.removeViewImmediate(dimmer_view);
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
		//throw new UnsupportedOperationException("Not yet implemented");
return null;
	}
}
