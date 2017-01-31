package com.mouse.backlightswitch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setActionOnToggleButtonCheckChanged();		
	}

private void setActionOnToggleButtonCheckChanged() {
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

@Override
protected void onStart() {
	super.onStart();
isBacklightActuallyOn();
}

private boolean isBacklightActuallyOn() {
	return true;
}

@Override
protected void onResume() {
	super.onResume();
	setChecked();
}

private void setChecked() {
	ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_button_switch);
	toggle.setChecked(!isBacklightActuallyOn());
}

private void switchBacklightOff() {
}

private void switchBacklightOn() {
}
}

