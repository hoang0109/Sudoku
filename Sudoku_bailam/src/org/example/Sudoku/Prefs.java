package org.example.Sudoku;

import android.R.bool;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity{
	public static final String KEY_PREF_SYNC_CONN = "hints";
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	public boolean getHit(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean value = preferences.getBoolean("hints", true);
		return value;
	}
	
}
