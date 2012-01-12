package org.rsternal.sudoku;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Robert Sternal (aka bobie)
 * @since December 2011.
 * @version 0.2
 *
 */
public class MainSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_settings);
	}
	
}
