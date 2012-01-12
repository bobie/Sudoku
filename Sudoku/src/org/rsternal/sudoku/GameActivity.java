package org.rsternal.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

/**
 * 
 * @author Robert Sternal (aka bobie)
 * @since December 2011
 * @version 0.2
 *
 */
public class GameActivity extends Activity {

	public static enum GAME_LEVEL {
		LOW(0), 
		MEDIUM(1), 
		HIGHT(2);
		
		private int level = 0;
		
		private GAME_LEVEL(int level) {
			this.level = level;
		}
		
		public int getLevel() {
			return this.level;
		}
	};
	
	public static final String PREF_PUZZLE_DATA_KEY = "puzzle_game_data";
	public static final String PREF_PUZZLE_GAME_TITLE_KEY = "puzzle_game_title";
	public static final String GAME_LEVEL_KEY = "game_level_key";
	public static final int PREF_PUZZLE_CONTINULE = -1;
	
	private static final String LOG_MARKER = "GameActivity";
	private static final String SH_PREF_FILE = "sudoku_pref_file.pref";
	private final String PUZZLE_SIMPLE_INIT_DATA =
		"360000000004230800000004200" +
		"070460003820000014500013020" +
		"001900000007048300000000045";
	private final String PUZZLE_MEDIUM_INIT_DATA =
		"650000070000506000014000005" +
		"007009000002314700000700800" +
		"500000630000201000030000097";
	private final String PUZZLE_HARD_INIT_DATA =
		"009000000080605020501078000" +
		"000000700706040102004000000" +
		"000720903090301080000000600";
	private final int[][][] usedBoxex = new int[9][9][];
	private int[] puzzles = new int[9 * 9];
	private ViewPuzzle vPuzzle;
	private int gameLevel;
	private SharedPreferences shPref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.shPref = getSharedPreferences(GameActivity.SH_PREF_FILE, MODE_PRIVATE);
		Log.d(GameActivity.LOG_MARKER, "fire onCreate(...)");
		
		gameLevel = getIntent().getIntExtra(GameActivity.GAME_LEVEL_KEY, 
				GameActivity.GAME_LEVEL.LOW.getLevel());
		
		if (gameLevel == GameActivity.PREF_PUZZLE_CONTINULE) {
			this.setTitle(this.shPref.getString(GameActivity.PREF_PUZZLE_GAME_TITLE_KEY, 
					this.getTitle() + " / " + getResources().getString(R.string.game_level_simple)));
			this.puzzles = getPuzzleDaraAsArray(this.shPref.getString(GameActivity.PREF_PUZZLE_DATA_KEY, 
					PUZZLE_SIMPLE_INIT_DATA));
		} else {
			for (GameActivity.GAME_LEVEL l : GameActivity.GAME_LEVEL.values()) {
				if (l.getLevel() == gameLevel) {
					this.puzzles = getPuzzles(l);
					break;
				}
			}
		
			String strLevel = "";
			switch(gameLevel) {
			case 0:
				strLevel = getResources().getString(R.string.game_level_simple);
				break;
			case 1:
				strLevel = getResources().getString(R.string.game_level_middle);
				break;
			case 2:
				strLevel = getResources().getString(R.string.game_level_hard);
			}
			this.setTitle(this.getTitle() + " / " + strLevel.toLowerCase());
		}
		
		calculateUsedBoxes();
		vPuzzle = new ViewPuzzle(this);
		setContentView(vPuzzle);
		vPuzzle.requestFocus();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		final Editor ed = this.shPref.edit();
		ed.putString(GameActivity.PREF_PUZZLE_DATA_KEY, getPuzzleDataAsString(puzzles));
		ed.putString(GameActivity.PREF_PUZZLE_GAME_TITLE_KEY, this.getTitle().toString());
		ed.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuOp = getMenuInflater();
		menuOp.inflate(R.menu.game_settings, menu);
		
		return true;
	}

	private int[] getPuzzles(GameActivity.GAME_LEVEL level) {
		switch(level.getLevel()) {
		case 0: return getPuzzleDaraAsArray(PUZZLE_SIMPLE_INIT_DATA);
		case 1:	return getPuzzleDaraAsArray(PUZZLE_MEDIUM_INIT_DATA);
		case 2: return getPuzzleDaraAsArray(PUZZLE_HARD_INIT_DATA);
		default: return getPuzzleDaraAsArray(PUZZLE_SIMPLE_INIT_DATA);
		}
	}
	
	protected int[] getPuzzleDaraAsArray(String data) {
		int[] arrayData = new int[data.length()];
		for (int i=0; i<data.length(); i++) {
			arrayData[i] = data.charAt(i) - '0';
		}
		return arrayData;
	}
	
	protected void showKeyboardOrError(int x, int y) {
		int[] boxes = getUsedBoxes(x, y);
		if (boxes.length == 9) {
			Toast toast = Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Dialog dlg = new Keyboard(this, boxes, vPuzzle);
			dlg.show();
		}
	}
	
	private String getPuzzleDataAsString(int[] data) {
		final StringBuffer strData = new StringBuffer();
		for (int p : data) {
			strData.append(p);
		}
		return strData.toString();
	}
	
	private int getValueOfPuzzlesBox(int x, int y) {
		return this.puzzles[y * 9 + x];
	}
	
	protected String getValueOfPuzzlesBoxAsString(int x, int y) {
		int val = getValueOfPuzzlesBox(x, y);
		if (val == 0)
			return "";
		else
			return String.valueOf(val); 
	}
	
	private void calculateUsedBoxes() {
		for (int x=0; x<9; x++)
			for (int y=0; y<9; y++)
				usedBoxex[x][y] = calculateUsedBoxes(x, y);
	}
	
	private int[] calculateUsedBoxes(int x, int y) {
		int[] usedNumbers = new int[9];
		
		// in vertical scope
		for (int i=0; i<9; i++) {
			if (i == y)
				continue;
			int t = getBox(x, i);
			if (t != 0)
				usedNumbers[t - 1] = t;
		}
			
		// in horizontal scope
		for (int i=0; i<9; i++) {
			if (i == x)
				continue;
			int t = getBox(i, y); 
			if (t != 0)
				usedNumbers[t - 1] = t;
		}
		
		// in block scope
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for (int i=startx; i<(startx + 3); i++) {
			for (int j=starty; j<(starty + 3); j++) {
				if (i == x && j == y)
					continue;
				int t = getBox(i, j);
				if (t != 0)
					usedNumbers[t - 1] = t;
			}
		}
		
		// remove space char (0 equivalent) from array
		int nNonZeroChars = 0;
		for (int e : usedNumbers) {
			if (e != 0)
				nNonZeroChars++;
		}
		int[] rArray = new int[nNonZeroChars];
		for (int e : usedNumbers)
			if (e != 0)
				rArray[rArray.length - nNonZeroChars--] = e;
		
		return rArray;
	}
	
	public boolean setIfBoxIsProperly(int x, int y, int value) {
		int[] boxes = getUsedBoxes(x, y);
		
		if (value != 0) {
			for (int box : boxes) {
				if (box == value)
					return false;
			}
		}
		setBox(x, y, value);
		calculateUsedBoxes();
		
		return true;
	}
	
	protected int[] getUsedBoxes(int x, int y) {
		return usedBoxex[x][y];
	}
	
	private int getBox(int x, int y) {
		return this.puzzles[y * 9 + x];
	}
	
	private void setBox(int x, int y, int value) {
		this.puzzles[y * 9 + x] = value;
	}
	
}
