package com.asigbe.awale;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity displays the rules of the game.
 * 
 * @author Delali Zigah
 */
public class RulesActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.rules);

		Typeface outOfAfricaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/out_of_africa.ttf");

		TextView awaleRulesTextView = (TextView) findViewById(R.id.awale_rules);
		awaleRulesTextView.setTypeface(outOfAfricaTypeFace);
		super.onCreate(savedInstanceState);
	}
}
