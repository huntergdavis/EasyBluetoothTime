package com.hunterdavis.easybluetoothtime;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyBluetoothTime extends Activity implements
		OnItemSelectedListener, OnInitListener {

	Integer MY_DATA_CHECK_CODE = 43556;
	String Shared_Preference_String = "EasyBluetoothTime";
	String Shared_Preference_Preference = "WhichAction";
	public static TextToSpeech mTts;
	public static boolean initialized;
	public static int currentlySelected;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialized = false;
		currentlySelected = 0;

		Spinner spinner = (Spinner) findViewById(R.id.intent_chooser);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.actions_array,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(this);

		// load up the shared preference if exists, 0 if not
		SharedPreferences settings = getSharedPreferences(
				Shared_Preference_String, MODE_PRIVATE);
		currentlySelected = settings.getInt(Shared_Preference_Preference, 0);
		spinner.setSelection(currentlySelected);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	}

	public void showError(Context context) {
		Toast.makeText(getBaseContext(), "An Error Has Occured!",
				Toast.LENGTH_SHORT).show();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		currentlySelected = pos;
		SharedPreferences settings = getSharedPreferences(
				Shared_Preference_String, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Shared_Preference_Preference, currentlySelected);
		editor.commit();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
				mTts.setLanguage(Locale.getDefault());
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onInit(int status) {
		initialized = true;
	}
}
