package org.nushackers.Minicraft;

import com.mojang.ld22.InputHandler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

public class DPad {
	private Rect mBounds = new Rect();
	private boolean mInGesture = false;
	private InputHandler mInputHandler;
	private int mDirection = NONE;
	private Path mShape = null;
	private Paint mPaint = new Paint();

	private static final int NONE = 0;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int UP = 3;
	private static final int DOWN = 4;
	
	public DPad() {
		mPaint.setColor(0x80ffffff);
	}
	
	public void setPosition(int x, int y, int size) {
		mBounds.set(x, y, x + size, y + size);
		final int width = size / 6;
		final int cx = x + size / 2;
		final int cy = y + size / 2;
		final int padding = 10;
		Region r = new Region();
		r.op(x + padding, cy - width, x + size - padding, cy + width, Region.Op.UNION);
		r.op(cx - width, y + padding, cx + width, y + size - padding, Region.Op.UNION);
		mShape = r.getBoundaryPath();
	}
	public void setInputHandler(InputHandler l) {
		mInputHandler = l;
	}
	public void draw(Canvas c) {
		if (mShape == null) return;
		c.drawPath(mShape, mPaint);
	}
	
	public boolean onTouch(MotionEvent e) {
		int x = (int) e.getX() - mBounds.left;
		int y = (int) e.getY() - mBounds.top;
		int height = mBounds.height();
		if (!mInGesture) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				if (x > 0 && y > 0 && x < height && y < height) {
					mInGesture = true;
				} else
					return false;
			} else
				return false;
		}
		if (e.getAction() == MotionEvent.ACTION_UP) {
			setDirection(NONE);
			mInGesture = false;
			return true;
		}
		/*
		 * Where are we? Divide our rect into four by drawing two diagonal
		 * lines. These are our controls.
		 * Lines:
		 *  x = y
		 *  x = height - y
		 */
		if (x > y) {
			/*
			 * Down or right.
			 */
			setDirection((x < height - y) ? UP : RIGHT);
		} else {
			/*
			 * Up or left.
			 */
			setDirection((x < height - y) ? LEFT : DOWN);
		}
		return true;
	}
	private void setDirection(int direction) {
		if (direction == mDirection || mInputHandler == null) return;
		mInputHandler.releaseAll();
		switch (direction) {
		case LEFT: mInputHandler.left.toggle(true); break;
		case RIGHT: mInputHandler.right.toggle(true); break;
		case UP: mInputHandler.up.toggle(true); break;
		case DOWN: mInputHandler.down.toggle(true); break;
		}
		mDirection = direction;
	}
}
