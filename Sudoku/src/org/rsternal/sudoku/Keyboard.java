package org.rsternal.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * 
 * @author Robert Sternal (aka bobie)
 * @since December 2011
 * @version 0.2
 *
 */
public class Keyboard extends Dialog {
	
	private final View[] keys = new View[9];
	private final ViewPuzzle vPuzzle;
	private final int[] usedBoxes;
	
	private View keyboard;
	
	public Keyboard(Context context, int[] usedBoxes, ViewPuzzle view) {
		super(context);
		this.usedBoxes = usedBoxes;
		this.vPuzzle = view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.title_keyboard);
		setContentView(R.layout.keyboard);
		findViews();
		for (int e : usedBoxes)
			if (e != 0)
				keys[e - 1].setVisibility(View.INVISIBLE);
		setKeysListener();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		int box = 0;
		switch(keyCode){
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE: 
			box = 0;
			break;
		case KeyEvent.KEYCODE_1:
			box = 1;
			break;
		case KeyEvent.KEYCODE_2:
			box = 2;
			break;
		case KeyEvent.KEYCODE_3:
			box = 3;
			break;
		case KeyEvent.KEYCODE_4:
			box = 4;
			break;
		case KeyEvent.KEYCODE_5:
			box = 5;
			break;
		case KeyEvent.KEYCODE_6:
			box = 6;
			break;
		case KeyEvent.KEYCODE_7:
			box = 7;
			break;
		case KeyEvent.KEYCODE_8:
			box = 8;
			break;
		case KeyEvent.KEYCODE_9:
			box = 9;
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		if (isValid(box))
			getResult(box);
		
		return true;
	}
	
	private void findViews() {
		keyboard = findViewById(R.id.keyboard);
		keys[0] = findViewById(R.id.keyboard_1);
		keys[1] = findViewById(R.id.keyboard_2);
		keys[2] = findViewById(R.id.keyboard_3);
		keys[3] = findViewById(R.id.keyboard_4);
		keys[4] = findViewById(R.id.keyboard_5);
		keys[5] = findViewById(R.id.keyboard_6);
		keys[6] = findViewById(R.id.keyboard_7);
		keys[7] = findViewById(R.id.keyboard_8);
		keys[8] = findViewById(R.id.keyboard_9);
	}

	private void setKeysListener() {
		for (int i=0; i<keys.length; i++) {
			final int t = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					getResult(t);
				}
			});
		}
		keyboard.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				getResult(0);				
			}
		});
	}
	
	private void getResult(int box) {
		vPuzzle.setSelectedBox(box);
		dismiss();
	}
	
	private  boolean isValid(int box) {
		for (int e : usedBoxes)
			if (e == box)
				return false;
		
		return true;
	}
}
