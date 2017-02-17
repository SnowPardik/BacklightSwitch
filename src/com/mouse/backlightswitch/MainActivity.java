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
private static final String PATH = "/sys/class/leds/wled:backlight/brightness";
private String brightnessLevelToBeRestored;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		Toast.makeText(getApplicationContext(), R.string.message_off, Toast.LENGTH_SHORT).show();
		switchBacklightOff();
	}

	private void onUnchecked() {
		Toast.makeText(getApplicationContext(), R.string.message_on, Toast.LENGTH_SHORT).show();
		switchBacklightOn();
	}

	private void switchBacklightOff() {
TextView textView = (TextView) findViewById(R.id.brightness_level);
textView.setText("Restore" + brightnessLevelToBeRestored);
	}

private void switchBacklightOn() {
}

@Override
protected void onStart() {
	super.onStart();
	String brightnessLevel = getCurrentBrightnessLevel(); 
setChecked(isBacklightCurrentlyOff(brightnessLevel));
showCurrentBrightnessLevel(brightnessLevel);
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
boolean backlightIsCurrentlyOff =  brightnessLevel.equals(zeroBrightness);
if (backlightIsCurrentlyOff == false)
	saveBrightnessLevelToBeRestored(brightnessLevel);
return backlightIsCurrentlyOff;
}

private void setChecked(boolean backlightIsCurrentlyOff) {
	ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_button_switch);
	toggle.setChecked(backlightIsCurrentlyOff);
}

private void showCurrentBrightnessLevel(String brightnessLevel) {
TextView textView = (TextView) findViewById(R.id.brightness_level);
textView.setText(brightnessLevel);
}

private String saveBrightnessLevelToBeRestored(String brightnessLevel) {
brightnessLevelToBeRestored = brightnessLevel;
return brightnessLevelToBeRestored;
}
}

