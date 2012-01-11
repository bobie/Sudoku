package org.rsternal.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class ViewPuzzle extends View {

	private static final String LOG_MARKER = "ViewPuzzle";
	private static final int VIEW_PUZZLE_ID = 11011011;
	
	private final GameActivity game;
	private final Rect rect = new Rect();
	
	private float width;
	private float height;
	private int selectedX;
	private int selectedY;
	
	public ViewPuzzle(Context context) {
		super(context);
		this.setId(ViewPuzzle.VIEW_PUZZLE_ID);
		this.game = (GameActivity) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	@Override
	protected  void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		this.width = width / 9f;
		this.height = height / 9f;
		getBox(selectedX, selectedY, rect);
		super.onSizeChanged(width, height, oldWidth, oldHeight);
	}
	
	private void getBox(int x, int y, Rect r) {
		rect.set((int)(x * this.width), (int)(y * this.height),
				(int)(x * this.width + this.width), (int)(y * this.height + this.height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		// TODO: rysuje plansze
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		Paint brightness = new Paint();
		brightness.setColor(getResources().getColor(R.color.puzzle_brightness));
		Paint brigth = new Paint();
		brigth.setColor(getResources().getColor(R.color.puzzle_bright));
		//Rysuje siatke
		for (int i=0; i<9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, brigth);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, brightness);
			canvas.drawLine(i * width, 0, i * width, getHeight(), brigth);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), brightness);
		}
		for (int i=0; i<9; i++) {
			if (i % 3 != 0) continue;
			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, brightness);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), brightness);
			
		}
		
		// TODO: rysuje cyfrey
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_top));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				canvas.drawText(
						this.game.getValueOfPuzzlesBoxAsString(i, j), 
						i * width + x, 
						j * height + y, 
						foreground);
			}
		}
		
		// TODO: Rysuje podpowiedzi
		// TODO: Rysuje wybran¹ wartoœæ
		Paint selectedBox = new Paint();
		selectedBox.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(rect, selectedBox);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(ViewPuzzle.LOG_MARKER, "onKeyDown: key code=" + keyCode 
				+ ", event {" + event + "}.");
		switch(keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			selectBox(selectedX, selectedY - 1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			selectBox(selectedX, selectedY + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			selectBox(selectedX - 1, selectedY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			selectBox(selectedX + 1, selectedX);
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE:
			setSelectedBox(0);
			break;
		case KeyEvent.KEYCODE_1:
			setSelectedBox(1);
			break;
		case KeyEvent.KEYCODE_2:
			setSelectedBox(2);
			break;
		case KeyEvent.KEYCODE_3:
			setSelectedBox(3);
			break;
		case KeyEvent.KEYCODE_4:
			setSelectedBox(4);
			break;
		case KeyEvent.KEYCODE_5:
			setSelectedBox(5);
			break;
		case KeyEvent.KEYCODE_6:
			setSelectedBox(6);
			break;
		case KeyEvent.KEYCODE_7:
			setSelectedBox(7);
			break;
		case KeyEvent.KEYCODE_8:
			setSelectedBox(8);
			break;
		case KeyEvent.KEYCODE_9:
			setSelectedBox(9);
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeyboardOrError(selectedY, selectedY);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		
		selectBox((int)(event.getX() / width), (int)(event.getY() / height));
		game.showKeyboardOrError(selectedX, selectedY);
		
		return true;
	}
	
	public void setSelectedBox(int box) {
		if (game.setIfBoxIsProperly(selectedX, selectedY, box))
			invalidate();
	}
	
	private void selectBox(int x, int y) {
		invalidate(rect);
		selectedX = Math.min(Math.max(x, 0), 8);
		selectedY = Math.min(Math.max(y, 0), 8);
		getBox(selectedX, selectedY, rect);
		invalidate(rect);
	}
	
}
