package com.nearsoft.OrientationTools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: javO
 * Date: 10/28/12
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetSpotMap extends View {

    public float getCurrentAzimuth() {
        return currentAzimuth;
    }

    public void setCurrentAzimuth(float currentAzimuth) {
        this.currentAzimuth = currentAzimuth;
    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    public void setCurrentPitch(float currentPitch) {
        this.currentPitch = currentPitch;
    }

    public float getHorizontalViewAngle() {
        return horizontalViewAngle;
    }

    private void setHorizontalViewAngle(float horizontalViewAngle) {
        this.horizontalViewAngle = horizontalViewAngle;
    }

    public float getInitialAzimuth() {
        return initialAzimuth;
    }

    public void setInitialAzimuth(float initialAzimuth) {
        this.initialAzimuth = initialAzimuth;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getCurrentRoll() {
        return currentRoll;
    }

    public void setCurrentRoll(float currentRoll) {
        this.currentRoll = currentRoll;
    }

    public boolean isCorrectPosition() {
        return correctPosition;
    }

    private void setCorrectPosition(boolean correctPosition) {
        this.correctPosition = correctPosition;
    }

    public Quadrant getCurrentQuadrant() {
        return currentQuadrant;
    }

    private void setCurrentQuadrant(Quadrant currentQuadrant) {
        this.currentQuadrant = currentQuadrant;
    }

    public float[] getMeridianPositions() {
        return meridianPositions;
    }

    private void setMeridianPositions(float[] meridianPositions) {
        this.meridianPositions = meridianPositions;
    }

    // Public properties
    private float initialAzimuth;
    private float currentRoll;
    private float currentPitch;
    private float currentAzimuth;
    private boolean visible;
    private Point center;
    private boolean correctPosition;
    private Quadrant currentQuadrant;
    private float[] meridianPositions;

    private final float EQUATOR_ANGLE = -90;

    public float getVerticalViewAngle() {
        return verticalViewAngle;
    }

    private void setVerticalViewAngle(float verticalViewAngle) {
        this.verticalViewAngle = verticalViewAngle;
    }

    // Private properties
    private float horizontalViewAngle;
    private float verticalViewAngle;
    private float meridianAngle;
    Paint spotPaint;
    Paint centerPaint;
    Paint textPaint;

    public TargetSpotMap(Context context, AttributeSet attrs){
        super(context, attrs);

        initPaints();
        this.center = new Point();
        this.currentQuadrant = null;
    }

    public void initSpotMap(float initialAzimuth, float horizontalViewAngle, float verticalViewAngle) {
        this.initialAzimuth = initialAzimuth;
        this.horizontalViewAngle = horizontalViewAngle;
        this.verticalViewAngle = verticalViewAngle;
        this.meridianPositions = calculateMeridianPositions();
        this.meridianAngle = calculateMeridianAngle(this.meridianPositions.length);


    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.center.set(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int tolerance = 8;
        float radius = 35f;


        Quadrant[] spots = calculateTargetSpots();
        for(Quadrant spot : spots) {

            if ((spot.getCenterPoint().x < center.x + tolerance) &&
                    (spot.getCenterPoint().x > center.x - tolerance) &&
                    (spot.getCenterPoint().y < center.y + tolerance) &&
                    (spot.getCenterPoint().y > center.y - tolerance)) {
                setCorrectPosition(true);
                setCurrentQuadrant(spot);
                centerPaint.setColor(Color.rgb(10, 180, 20)); // green
                radius = 40f;
            }
            else {
                setCorrectPosition(false);
                centerPaint.setColor(Color.rgb(20,200,210)); // aqua
                radius = 35f;
            }


            canvas.drawCircle(spot.getCenterPoint().x, spot.getCenterPoint().y, 38f, this.spotPaint);
        }

        canvas.drawCircle(center.x, center.y, radius, this.centerPaint);
        if (isCorrectPosition()) {
            canvas.drawText(String.valueOf(getCurrentQuadrant().getParallel()) + "-" +
                    String.valueOf(getCurrentQuadrant().getMeridian()), center.x, center.y + 8, textPaint);

        }
    }

    private void initPaints() {
        spotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spotPaint.setAlpha(180);
        spotPaint.setColor(Color.rgb(20, 200, 210)); // aqua
        spotPaint.setStyle(Paint.Style.STROKE);
        spotPaint.setStrokeWidth(6f);

        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setAlpha(180);
        centerPaint.setColor(Color.rgb(200, 0, 20)); // red
        centerPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
        textPaint.setColor(Color.rgb(0,0,0));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(24);
    }

    private List<Float> calculateVisibleSpotsAngles(float startViewAngle, float endViewAngle) {
        List<Float> divisionsList = new ArrayList<Float>();

        for(float division : this.meridianPositions) {
            if (Graphics.isInRange(division, startViewAngle, endViewAngle) &&
                Graphics.isInRange(getCurrentRoll(), Graphics.angleSubtraction(EQUATOR_ANGLE, getVerticalViewAngle()), Graphics.angleSum(EQUATOR_ANGLE, getVerticalViewAngle()))
                ) {
                divisionsList.add(division);
            }
        }
        return divisionsList;
    }

    private Quadrant[] calculateTargetSpots() {
        float startHorizontalAngle = Graphics.angleSubtraction(currentAzimuth, (horizontalViewAngle / 2f));
        float endHorizontalAngle = Graphics.angleSum(currentAzimuth, (horizontalViewAngle / 2f));
        float startVerticalAngle = Graphics.angleSubtraction(currentRoll, (verticalViewAngle / 2f));
        float x, y;
        List<Float> visibleSpots = calculateVisibleSpotsAngles(startHorizontalAngle, endHorizontalAngle);
        Quadrant[] spots = new Quadrant[visibleSpots.size()];
        for(int i = 0; i < spots.length; i++) {
            x = horizontalAngleToXCoordinate(visibleSpots.get(i).floatValue(), endHorizontalAngle);
            y = verticalAngleToYCoordinate(EQUATOR_ANGLE, startVerticalAngle);
            spots[i] = new Quadrant(new Point((int)x, (int)y), 0, (int)(Graphics.base180to360(visibleSpots.get(i)) / this.meridianAngle));
        }
        return spots;
    }

    private float horizontalAngleToXCoordinate(float angle, float leftEdgeAngle) {
        float relativeAngle = Graphics.angleSubtraction(leftEdgeAngle, angle);
        return (relativeAngle * (float)getWidth()) / horizontalViewAngle;
    }

    private float verticalAngleToYCoordinate(float angle, float startAngle) {
        float relativeAngle = -Graphics.angleSubtraction(startAngle, angle);
        return (relativeAngle * (float)getHeight()) / verticalViewAngle;
    }

    private float[] calculateMeridianPositions() {
        int numberOfMeridians = calculateNumberOfMeridians(this.horizontalViewAngle);
        float divisionAngle = calculateMeridianAngle(numberOfMeridians);
        float[] divisions = new float[numberOfMeridians];
        for (int i = 0; i < numberOfMeridians; i++) {
            divisions[i] = Graphics.angleSum(initialAzimuth, divisionAngle * i);
        }
        return divisions;

    }

    private float calculateMeridianAngle(int numberOfMeridians) {
        return 360f / (float)numberOfMeridians;
    }

    public static int calculateNumberOfMeridians(float horizontalViewAngle) {
        int numberOfDivisions = (int)Math.ceil(1.2 * 360.0 / (double)horizontalViewAngle);
        switch(numberOfDivisions) {
            case 7:
            case 11:
            case 14:
                numberOfDivisions++;
                break;
            case 13:
                numberOfDivisions += 2;
                break;
        }
        return numberOfDivisions;
    }
}
