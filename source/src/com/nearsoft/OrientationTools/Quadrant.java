package com.nearsoft.OrientationTools;

import android.graphics.Point;

/**
 * Created with IntelliJ IDEA.
 * User: javO
 * Date: 10/28/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Quadrant {
    private Point centerPoint;
    private int parallel;
    private int meridian;

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }


    public int getMeridian() {
        return meridian;
    }

    public void setMeridian(int meridian) {
        this.meridian = meridian;
    }

    public int getParallel() {
        return parallel;
    }

    public void setParallel(int parallel) {
        this.parallel = parallel;
    }

    public Quadrant(Point centerPoint, int parallel, int meridian) {
        setCenterPoint(centerPoint);
        setParallel(parallel);
        setMeridian(meridian);
    }
}