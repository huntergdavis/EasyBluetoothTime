package com.hunterdavis.easybluetoothtime;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;


public class MediaButtonReceiver extends BroadcastReceiver {

	public MediaButtonReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		String intentAction = intent.getAction();
		if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
			return;
		}
		KeyEvent event = (KeyEvent) intent
				.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
		if (event == null) {
			return;
		}
		int action = event.getAction();
		if (action != KeyEvent.ACTION_DOWN) {
			return;
		}
		if(event.getRepeatCount() > 0) {
			return;
		}
		
		
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_MEDIA_NEXT:
			// something for next
			if (EasyBluetoothTime.currentlySelected == 0) {
				abortBroadcast();
				sayTheTime();
			}
			return;
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			// something for previous
			if (EasyBluetoothTime.currentlySelected == 1) {
				abortBroadcast();
				sayTheTime();
			}
			return;
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			// something for play/pause
			if (EasyBluetoothTime.currentlySelected == 2) {
				abortBroadcast();
				sayTheTime();
			}
			return;
		}
		return;
	}
	
	private void sayTheTime() {
		if (!EasyBluetoothTime.initialized) {
			return;
		}
		
		// initialize hashmap of stream parameters - set to alarm status
		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
				String.valueOf(AudioManager.STREAM_MUSIC));

		// get the time
		Time now = new Time();
		now.setToNow();
		int hour = now.hour;
		if (hour > 12) {
			hour = hour - 12;
		}

		String timeToSpeak = "The Time Is " + hour + " " + now.minute;

		// say the time;
		EasyBluetoothTime.mTts.speak(timeToSpeak, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
	}	
}
