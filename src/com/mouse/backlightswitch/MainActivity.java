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
		ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_button_switch);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				Toast.makeText(getApplicationContext(), R.string.message_off, Toast.LENGTH_SHORT).show();;
			} else{
				Toast.makeText(getApplicationContext(), R.string.message_on, Toast.LENGTH_SHORT).show();;
			}
		}
		});	
	}
}
