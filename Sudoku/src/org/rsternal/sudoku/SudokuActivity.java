package org.rsternal.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SudokuActivity extends Activity implements OnClickListener  {
	
	private static final String LOG_TAG = "### {SudokuActivitiy} # ";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);
        View btnNewGame = findViewById(R.id.btn_new_game);
        btnNewGame.setOnClickListener(this);
        View btnInformation = findViewById(R.id.btn_info);
        btnInformation.setOnClickListener(this);
    }

    public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.btn_new_game:
			openDialogNewGame();
			break;
		case R.id.btn_info:
			Intent i = new Intent(this, InformationActivity.class);
			startActivity(i);
			break;
		case R.id.btn_continue:
			runGame(GameActivity.PREF_PUZZLE_CONTINULE);
			break;
		default:
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mnmExtender = getMenuInflater();
		mnmExtender.inflate(R.menu.menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, MainSettingsActivity.class));
			break;
		default:
			return false;
		}
		
		return false;
	}
	
	private void openDialogNewGame() {
		new AlertDialog.Builder(this)
			.setTitle(R.string.title_new_game)
			.setItems(R.array.difficult,
					new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							runGame(which);
							
						}
					}).show();
	}
	
	private void runGame(int i) {
		Log.d(SudokuActivity.LOG_TAG, "[ " 
				+ System.currentTimeMillis() 
				+ " ] Klikniêto ... ( " 
				+ i + " ).");
		Intent intent = new Intent(SudokuActivity.this, GameActivity.class);
		intent.putExtra(GameActivity.GAME_LEVEL_KEY, i);
		startActivity(intent);
	}
	
}