package com.example.inzynierka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomCanvas  extends View {
    private Paint paint;
    private Rect rectangle;
    public float distance;
    public int angle;
    public float width;
    public float height;
    public float circleX, circleY;
    private int[] rectXY;
    public boolean enable;

    public CustomCanvas(Context context){
        super(context);
        init();
    }
    public CustomCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(enable) {
            circleX = event.getX();
            circleY = event.getY();
            limit();
            invalidate();
        }
         switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                return false;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        circleX = width/2;
        circleY = (int)(0.5 * height);
        int rectWidth = (int) (0.9 * width);
        int rectHeight = (int) (rectWidth * 1.8);
        int x1 = (int) (0.05 * width);
        int y1 = x1;
        int x2 = x1 + rectWidth;
        int y2 = x1 + rectHeight;
        rectangle.set(x1, y1, x2, y2);
        rectXY = new int[]{x1, y1, x2, y2};
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        rectangle.set(rectXY[0],rectXY[1],rectXY[2],rectXY[3]);
        if (enable) paint.setColor(Color.BLACK);
        else paint.setColor(Color.GRAY);
        canvas.drawRect(rectangle, paint);

        paint.setColor(Color.WHITE);
        rectangle.inset(6,6);
        canvas.drawRect(rectangle,paint);

        if (enable) paint.setColor(Color.rgb(51, 102, 255));
        else paint.setColor(Color.GRAY);
        rectangle.inset(30,30);
        canvas.drawRect(rectangle,paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        canvas.drawLine(width/2, rectangle.top, width/2, rectangle.bottom, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
        int y = rectangle.top + rectangle.height()/2;
        canvas.drawLine((int)(0.02 * width), y, (int) (width * 0.98), y, paint);

        if (enable) paint.setColor(Color.RED);
        else paint.setColor(Color.DKGRAY);
        canvas.drawCircle(circleX, circleY, 40, paint);
        paint.setStrokeWidth(7);
        canvas.drawLine(width/2, rectangle.bottom, circleX, circleY, paint);
        distance = (float) Math.sqrt((width/2 - circleX)*(width/2 - circleX) + (rectangle.bottom - circleY)*(rectangle.bottom - circleY));
        angle =  (int) Math.toDegrees(Math.atan((rectangle.bottom - circleY) / (width/2 - circleX)));
        if (angle < 0) angle += 180;
    }

    private void init() {
        paint = new Paint();
        rectangle = new Rect();
    }

    private void limit(){
        if (circleX > rectangle.right) circleX = rectangle.right;
        if (circleX < rectangle.left) circleX = rectangle.left;
        if (circleY > rectangle.bottom / 3) circleY = rectangle.bottom / 3;
        if (circleY < rectangle.top) circleY = rectangle.top;
    }
}
