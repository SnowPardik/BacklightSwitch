package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;

import android.app.Notification;
import android.app.NotificationManager;
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
	private static final float DIM_AMOUNT = 1.0f;
	private static final int NOTIFICATION_ID = 1;
	private static final String BRIGHTNESS_LEVEL_TO_BE_RESTORED_IF_STARTS_WITH_ZERO_BRIGHTNESS = "289";
	private static final String PATH = "/sys/class/leds/wled:backlight/brightness";
	private static final String NOTIFICATION_ACTION = "com.mouse.backlightswitch.notification";
	private static final String ZERO_BRIGHTNESS = "0";

	private DimmerView dimmer_view;
	private Notification.Builder nb;
	private String brightnessLevelToBeRestored;

	@Override
	public void onCreate() {
		super.onCreate();

		dimmer_view = new DimmerView(this);

		String brightnessLevel = getCurrentBrightnessLevel();
		setBrightnessLevelToBeRestoredIfStartsWithZeroBrightness(brightnessLevel);
		createNotification(brightnessLevel);

		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ifScreenIsDimmed();
				updateNotification();
			}
		}, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}

	private void ifScreenIsDimmed() {
		if (dimmer_view.isAttachedToWindow()) {
			switchBacklightOff();
			Toast.makeText(this, getResources().getString(R.string.is_off), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String intentAction = intent.getAction();
		if (NOTIFICATION_ACTION.equals(intentAction)) {
			handleNotificationClick();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void createNotification(String brightnessLevel) {
		String notificationText = getNotificationText(brightnessLevel);
		Intent i = new Intent(NOTIFICATION_ACTION, null, this, BacklightSwitchService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nb = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentText(notificationText)
				.setContentIntent(pi).setShowWhen(false).setPriority(Notification.PRIORITY_MIN);
		Notification n = nb.build();
		nm.notify(NOTIFICATION_ID, n);
		startForeground(NOTIFICATION_ID, n);
	}

	private void handleNotificationClick() {
		String brightnessLevel = getCurrentBrightnessLevel();
		if (brightnessLevel.equals(ZERO_BRIGHTNESS)) {
			switchBacklightOn();
			removeDimmerFromTheScreen();
			Toast.makeText(this, "Восстанавливаю" + brightnessLevelToBeRestored, Toast.LENGTH_SHORT).show();
			updateNotification();
		} else {
			brightnessLevelToBeRestored = brightnessLevel;
			switchBacklightOff();
			putDimmerOverTheScreen();
			Toast.makeText(this, "Выключаю подсветку, буду восстанавливать" + brightnessLevelToBeRestored,
					Toast.LENGTH_SHORT).show();
			updateNotification();
		}
	}

	private String getCurrentBrightnessLevel() {
		File f = new File(PATH);
		String brightnessLevel = "";
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr, 8);
			sb.append(br.readLine());
			br.close();
			brightnessLevel = sb.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return brightnessLevel;
	}

	private void setBrightnessLevelToBeRestoredIfStartsWithZeroBrightness(String brightnessLevel) {
		if (brightnessLevel.equals(ZERO_BRIGHTNESS)) {
			brightnessLevelToBeRestored = BRIGHTNESS_LEVEL_TO_BE_RESTORED_IF_STARTS_WITH_ZERO_BRIGHTNESS;
		}
	}

	private String getNotificationText(String brightnessLevel) {
		String notificationText = "";
		if (brightnessLevel.equals(ZERO_BRIGHTNESS)) {
			notificationText = getResources().getString(R.string.is_off);
		} else {
			notificationText = getResources().getString(R.string.is_on);
		}
		return notificationText;
	}

	private void switchBacklightOn() {
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			dos.writeBytes("echo " + brightnessLevelToBeRestored + " >" + PATH + "\n");
			dos.writeBytes("exit\n");
			dos.flush();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void switchBacklightOff() {
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			dos.writeBytes("echo " + ZERO_BRIGHTNESS + " >" + PATH + "\n");
			dos.writeBytes("exit\n");
			dos.flush();
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void putDimmerOverTheScreen() {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		LayoutParams dimmer_params = new LayoutParams();
		dimmer_params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		dimmer_params.flags |= LayoutParams.FLAG_DIM_BEHIND;
		dimmer_params.dimAmount = DIM_AMOUNT;
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

		wm.addView(dimmer_view, dimmer_params);
	}

	private void removeDimmerFromTheScreen() {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		wm.removeViewImmediate(dimmer_view);
	}

	private void updateNotification() {
		String brightnessLevel = getCurrentBrightnessLevel();
		String notificationText = getNotificationText(brightnessLevel);
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nb.setContentText(notificationText);
		nm.notify(NOTIFICATION_ID, nb.build());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		// throw new UnsupportedOperationException("Not yet implemented");
		return null;
	}
}
