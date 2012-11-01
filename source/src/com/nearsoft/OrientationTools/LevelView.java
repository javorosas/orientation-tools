package com.nearsoft.OrientationTools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: javO
 * Date: 10/28/12
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class LevelView extends View {
    private final float MAX_PITCH = 30f;
    private final float PITCH_RELATIVE = MAX_PITCH * 2f;

    private boolean pitchOK;
    private float pitch;
    private Paint linePaint;

    public boolean isPitchOK() {
        return pitchOK;
    }

    private void setPitchOK(boolean pitchOK) {
        this.pitchOK = pitchOK;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public LevelView(Context context, AttributeSet attrs){
            super(context, attrs);
            initPaints();
    }

    private void initPaints() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setAlpha(200);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Point[] points = getDrawingPoints();
        // Pitch line
        canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, linePaint);
    }

    private Point[] getDrawingPoints(){
        float width = getWidth();
        float height = getHeight();

        // Get pitch for graphics
        float pitch;
        if (this.pitch > MAX_PITCH){
            pitch = MAX_PITCH;
        }
        else if (this.pitch < -MAX_PITCH){
            pitch = 0 - MAX_PITCH;
        }
        else {
            pitch = this.pitch;
        }
        if (pitch > -3 && pitch < 3) {
            linePaint.setColor(Color.GREEN);
            setPitchOK(true);
        }
        else {
            linePaint.setColor(Color.RED);
            setPitchOK(false);
        }

        pitch = MAX_PITCH - pitch;

        // Pitch line positions
        Point[] points = new Point[2];
        points[0] = new Point(0, (int)(pitch * getHeight() / PITCH_RELATIVE));
        points[1] = new Point((int)getWidth(), getHeight() - points[0].y);

        return points;
    }
}
