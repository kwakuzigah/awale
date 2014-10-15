package com.asigbe.awale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.asigbe.utils.ReportSender;
import com.asigbe.view.ViewTools;

/**
 * This activity display the main menu.
 * 
 * @author Delali Zigah
 */
public class MainActivity extends Activity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		ReportSender.install(getApplicationContext());
		
		setContentView(R.layout.main);

		ViewTools.inflateView(this, R.layout.awale_layout);

		Typeface outOfAfricaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/out_of_africa.ttf");

		TextView awaleTitleTextView = (TextView) findViewById(R.id.awale_title);
		awaleTitleTextView.setTypeface(outOfAfricaTypeFace);

		Typeface africanTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/african.ttf");

		Button newGameButton = (Button) findViewById(R.id.new_game_button);
		newGameButton.setTypeface(africanTypeFace);
		newGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PlayActivity.class));
			}
		});

		Button rulesButton = (Button) findViewById(R.id.rules_button);
		rulesButton.setTypeface(africanTypeFace);
		rulesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, RulesActivity.class));
			}
		});

		Button creditsButton = (Button) findViewById(R.id.credits_button);
		creditsButton.setTypeface(africanTypeFace);
		creditsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						CreditsActivity.class));
			}
		});

		PackageManager pm = getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(getPackageName(), 0);
			TextView demoVersionButton = (TextView) findViewById(R.id.demo_version);
			demoVersionButton.setText(getString(R.string.demo_version) + " "
					+ pi.versionName);
		} catch (NameNotFoundException e) {
		}
		super.onCreate(savedInstanceState);
	}
	// @Override
	// protected void onNewIntent(Intent intent) {
	// boolean mustResume = intent.getExtras().getBoolean(RESUME_KEY, false);
	// if (mustResume) {
	// startActivity(new Intent(MainActivity.this, PlayActivity.class));
	// }
	// cancelPendingItent();
	// super.onNewIntent(intent);
	// }
}