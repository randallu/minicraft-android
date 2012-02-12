package org.nushackers.Minicraft;

import org.nushackers.Minicraft.MultiButton.MultiTouchListener;

import com.mojang.ld22.InputHandler;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ControlPad extends LinearLayout {
	private MultiButton button1;
	private MultiButton button2;
	private DPad dpad;
	
	private View pad;

	public ControlPad(Context context, AttributeSet attrs) {
		super(context, attrs);
		button1 = new MultiButton(context, attrs);
		button2 = new MultiButton(context, attrs);
		dpad = new DPad();
		init();
	}
	public ControlPad(Context context) {
		super(context);
		button1 = new MultiButton(context);
		button2 = new MultiButton(context);
		init();
	}
	
	
	private void init() {
		setOrientation(LinearLayout.HORIZONTAL);
		pad = new View(getContext());
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		removeView(button1);
		removeView(button2);

		int padW = getMeasuredWidth()-(getMeasuredHeight()*3);
		int joyWidth = ((getMeasuredWidth()-padW)/3);
		LayoutParams joyLParams = new LayoutParams(joyWidth,getMeasuredHeight());
		button1.setLayoutParams(joyLParams);
		button2.setLayoutParams(joyLParams);
		button1.TAG = "L";
		button2.TAG = "R";
		addView(button1);
		addView(button2);
		
		ViewGroup.LayoutParams padLParams = new ViewGroup.LayoutParams(padW,getMeasuredHeight());
		removeView(pad);
		pad.setLayoutParams(padLParams);
		addView(pad);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		button1.setTouchOffset(button1.getLeft(), button1.getTop());
		button2.setTouchOffset(button2.getLeft(), button2.getTop());
		int height = bottom - top;
		dpad.setPosition((right - left) - height, 0, height);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		dpad.draw(canvas);
		super.dispatchDraw(canvas);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return	dpad.onTouch(event) |
				button1.dispatchTouchEvent(event) |
				button2.dispatchTouchEvent(event);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return	dpad.onTouch(event) |
				button1.onTouchEvent(event) |
				button2.onTouchEvent(event);
	}
	
	public void setJoystickListener(InputHandler l){
		dpad.setInputHandler(l);
	}
	
	public void setButton1Listener(MultiTouchListener l) {
		button1.setTouchEventListener(l);
	}
	public void setButton2Listener(MultiTouchListener l) {
		button2.setTouchEventListener(l);
	}

}
