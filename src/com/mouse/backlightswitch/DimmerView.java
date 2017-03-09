package com.mouse.backlightswitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DimmerView extends LinearLayout {

	public DimmerView(Context context) {
		super(context);
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.dimmer_view, this, true);
}

}
