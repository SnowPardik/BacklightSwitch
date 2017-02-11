package com.mouse.backlightswitch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//TextView textView = (TextView) findViewById(R.id.brightness_level);
		//textView.setText("Brightness level");
		setToggleListener();		
	}

	private void setToggleListener() {
		ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_button_switch);
	toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked)
				onChecked();
			else
				onUnchecked();
				}
		});	
	}

	private void onChecked() {
		switchBacklightOff();
		Toast.makeText(getApplicationContext(), R.string.message_off, Toast.LENGTH_SHORT).show();
	}

	private void onUnchecked() {
		switchBacklightOn();
		Toast.makeText(getApplicationContext(), R.string.message_on, Toast.LENGTH_SHORT).show();	
	}

	private void switchBacklightOff() {
	}

	private void switchBacklightOn() {
	}

@Override
protected void onStart() {
	super.onStart();
isBacklightCurrentlyOn();
setChecked();
}

private boolean isBacklightCurrentlyOn() {
return true;
}

private void setChecked() {
	ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_button_switch);
	toggle.setChecked(!isBacklightCurrentlyOn());
}

@Override
protected void onResume() {
	super.onResume();
	getCurrentBrightnessLevel();
}

private void getCurrentBrightnessLevel() {
	String path = "/sys/class/leds/wled:backlight/brightness";
File f = new File(path);
StringBuilder sb = new StringBuilder();
String s;
String brightnessLevel;
try {
FileReader fr = new FileReader(f);
BufferedReader br = new BufferedReader(fr);
s = br.readLine();
br.close();
		sb.append(s);
brightnessLevel = sb.toString();
TextView textView = (TextView) findViewById(R.id.brightness_level);
textView.setText(brightnessLevel);
} catch (Exception e) {
	// TODO: handle exception
}
}
}
