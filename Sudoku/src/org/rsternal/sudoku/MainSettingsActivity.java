package org.rsternal.sudoku;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MainSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_settings);
	}
	
}
