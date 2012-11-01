package com.nearsoft.OrientationTools;
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 8/20/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Graphics {
    public static float getLineSlope(float x1, float y1, float x2, float y2) {
        float m = 0;
        // from the classic line equation
        m = (y2 - y1) / (x2 - x1);
        return m;
    }

    public static float getYFromX(float x, float m, float b) {
        // line equation y = mx + b
        return m * x + b;
    }

    public static float angleSum(float angle1, float angle2){
        float result = angle1 + angle2;
        return correctAngle(result);
    }

    public static float angleSubtraction(float angle1, float angle2) {
        float result = angle1 - angle2;
        return correctAngle(result);
    }

    public static boolean isInRange(float angle, float startRange, float endRange) {
        float correction = 0 - startRange;
        angle = angleSum(angle, correction);
        startRange = angleSum(startRange, correction);
        endRange = angleSum(endRange, correction);

        if (angle < endRange && angle > startRange)
            return true;
        else
            return false;
    }

    public static float correctAngle(float angle)
    {
        if (angle > 360) {
            angle = angle - 360 * ((float)Math.floor(Math.abs(angle) / 360));
        }
        else if (angle <= -360) {
            angle = angle + 360 * ((float)Math.floor(Math.abs(angle) / 360));
        }

        if (angle > 180){
            angle = angle - 360;
        }
        else if(angle <= -180){
            angle = angle + 360;
        }

        return angle;
    }

    public static float base180to360(float angle) {
        return angle + 180;
    }

    public static float[] polarToCartesian(float radius, float angleInRadians) {
        float[] xy = new float[2];
        xy[0] = (float)Math.cos( angleInRadians ) * radius;
        xy[1] = (float)Math.sin( angleInRadians ) * radius;
        return xy;
    }

    public static float[] cartesianToPolar(float x, float y) {
        float[] polar = new float[2];
        polar[0] = (float)Math.sqrt( x * x + y * y );
        polar[1] = (float)Math.atan2(y, x);
        return polar;
    }
}
