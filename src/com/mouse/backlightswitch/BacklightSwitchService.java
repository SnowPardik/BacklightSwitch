package com.mouse.backlightswitch;

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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotification();
		switchBacklight(intent.getAction());
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

	private void switchBacklight(String intentAction) {
		Toast.makeText(getApplicationContext(), "Получаю интент" + intentAction, Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
