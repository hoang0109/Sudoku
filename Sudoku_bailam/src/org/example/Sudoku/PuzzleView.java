package org.example.Sudoku;

import android.R.bool;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class PuzzleView extends View {
	private static final String TAG ="Sudoku" ;
	private final Game game ;
	private float width ;
	private float height ;
	private int selX ;
	private int selY ;
	private Rect selRect = new Rect();

	public PuzzleView(Context context){
		super(context);
		
		this.game = (Game) context ;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	//su kien kich co man hinh thay doi
	@Override
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		width = w/9f ;
		height = h/9f ;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width " + width + ", height" + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	// ve ban game
	public void getRect(int x, int y, Rect rect){
		rect.set((int) (x*width),(int) (y*height),(int) (x*width + width), (int) (y*height + height));
	}
	
	//ve cac dong ke
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas){
		//ve nen
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0,0,getWidth(),getHeight(),background);
		// ve dong ke
			// ve dong ke lon
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
			// ve dong ke chia cac o va tao do noi
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
			
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		//ve dong ke chia cac o
		for(int i = 0;i < 9 ; i++){
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
		}
		//ve dong ke chia cac nhom
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0)
			continue;
			canvas.drawLine(0, i * height, getWidth(), i * height,
			dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height
			+ 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1,
			getHeight(), hilite);
			}
		
		// Vẽ các so nam trong cac o
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height*0.75f);
		foreground.setTextScaleX(width/height);
		foreground.setTextAlign(Paint.Align.CENTER);
		Paint foreground2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground2.setStyle(Style.FILL);
		foreground2.setTextSize(height*0.75f);
		foreground2.setTextScaleX(width/height);
		foreground2.setTextAlign(Paint.Align.CENTER);
		foreground2.setColor(getResources().getColor(R.color.puzzle_foreground2));
		FontMetrics fm = foreground.getFontMetrics();
		
		float x = width/2 ;
		float y = height/2 - (fm.ascent + fm.descent)/2 ;
		for(int i=0; i < 9; i++)
			for(int j=0;j<9;j++)
			{
				boolean test = this.game.getTileString_old(i, j).equalsIgnoreCase("");
				if(test==true){
					canvas.drawText(this.game.getTileString_old(i, j), i*width + x,j*height + y, foreground2);}
				if (test==false) {
					canvas.drawText(this.game.getTileString_old(i, j), i*width + x,j*height + y, foreground);
				} 
				
				
			}
		// Draw the selection
		Log.d(TAG,"selRect=" + selRect);
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
	}
	// bat su kien nhan phim
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown: keycode" + keyCode +",event= " + event);
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX,selY - 1);
			break ;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX,selY + 1);
			break ;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1,selY);
			break ;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1,selY);
			break ; 
		/*xu ly cac so tu 0-9*/
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break ;
		case KeyEvent.KEYCODE_1:     setSelectedTile(1); break ;
		case KeyEvent.KEYCODE_2:     setSelectedTile(2); break ;
		case KeyEvent.KEYCODE_3:     setSelectedTile(3); break ;
		case KeyEvent.KEYCODE_4:     setSelectedTile(4); break ;
		case KeyEvent.KEYCODE_5:     setSelectedTile(5); break ;
		case KeyEvent.KEYCODE_6:     setSelectedTile(6); break ;
		case KeyEvent.KEYCODE_7:     setSelectedTile(7); break ;
		case KeyEvent.KEYCODE_8:     setSelectedTile(8); break ;
		case KeyEvent.KEYCODE_9:     setSelectedTile(9); break ;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX,selY);
			break ;
		default:
			return super.onKeyDown(keyCode, event);
			
		}
		return true;
	}
	//phuong thuc chon 
	private void select(int x, int y)
	{
		invalidate(selRect);
		selX = Math.min(Math.max(x,0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}
	//Hien thi keyPad cho nguoi choi nhan vao
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		
		select((int) (event.getX() / width),
		(int) (event.getY() / height));
		
  			 if(this.game.mypuzzle[selY][selX]==0){
  				game.showKeypadOrError(selX, selY);
  				Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
  				return true;
  			 }
  			 else {
				return false;
			}
  			
		
		
	}
	// phuong thuc setSelectedTile() lay ki tu tren ban phim
	void setSelectedTile(int tile){
		if(game.setTileIfValid(selX,selY,tile)){
			invalidate();
		}else {
			Log.d(TAG, "setSelectedTile : invalid " + tile );
			startAnimation(AnimationUtils.loadAnimation(game,R.anim.shake));
		}
	}
}
