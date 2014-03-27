package org.example.Sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.View;

public class PuzzleView6 extends View{
	private Game6 game6;
	private float width , height;
	private int selX , selY;
	private int Size = 6;
	private final Rect selRect = new Rect();
	public PuzzleView6(Context context){
		super(context);
		this.game6=(Game6) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w/6f;
		height = h/6f;
		getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	public void getRect(int x, int y, Rect rect){
		rect.set((int) (x*width),(int) (y*height),(int) (x*width + width), (int) (y*height + height));
	}
	@Override
	protected void onDraw(Canvas canvas) {
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
				for(int i = 0;i < Size ; i++){
					canvas.drawLine(0, i * height, getWidth(), i * height, light);
					canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
					canvas.drawLine(i * width, 0, i * width, getHeight(), light);
					canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
				}
				//ve dong ke chia cac nhom
				for (int i = 0; i < Size; i++) {
					if (i % 2 == 0)
					{
					canvas.drawLine(0, i * height, getWidth(), i * height,
					dark);
					canvas.drawLine(0, i * height + 1, getWidth(), i * height
					+ 1, hilite);
					}
					if(i%3==0){
						canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
						canvas.drawLine(i * width + 1, 0, i * width + 1,
						getHeight(), hilite);
					}
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
				for(int i=0; i < Size; i++)
					for(int j=0;j<Size;j++)
					{
							canvas.drawText(this.game6.getTileString(i, j), i*width + x,j*height + y, foreground);
						
					}
				// Draw the selection
				Paint selected = new Paint();
				selected.setColor(getResources().getColor(R.color.puzzle_selected));
				canvas.drawRect(selRect, selected);
		super.onDraw(canvas);
		
	}
	
}
