package com.nearsoft.OrientationTools.Compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.nearsoft.OrientationTools.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: javO
 * Date: 10/27/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Compass {

    private SensorManager mSensorManager;
    private Sensor mGyroSensor, mRotSensor;//, mRotSensor, mMagSensor;
    public CompassEvent ValuesChanged;

    private float azimuth;
    private float pitch;
    private float roll;

    private float[] rotMatrix;

    public Compass(Activity mainActivity){
        mSensorManager = (SensorManager) mainActivity.getSystemService(mainActivity.SENSOR_SERVICE);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mRotSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        rotMatrix = new float[9];
        startSensing();
    }

    public float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void startSensing(){
        mSensorManager.registerListener(mGyroListener, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mRotListener, mRotSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSensing(){
        mSensorManager.unregisterListener(mGyroListener, mGyroSensor);
        mSensorManager.unregisterListener(mRotListener, mRotSensor);
    }

    private SensorEventListener mGyroListener = new SensorEventListener() {

        private static final float MIN_TIME_STEP = (1f / 40f);
        private long mLastTime = System.currentTimeMillis();
        private float mRotationX;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // not used
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float azimuth = values[0];

            // Compense drift
            azimuth *= 0.96;

            // Calculate time diff
            long now = System.currentTimeMillis();
            float timeDiff = (now - mLastTime) / 1000f;
            mLastTime = now;


            if (timeDiff > 1) {
                // Make sure we don't go bananas after pause/resume
                timeDiff = MIN_TIME_STEP;
            }


            mRotationX += azimuth * timeDiff;

            setAzimuth(Graphics.correctAngle((float) Math.toDegrees(mRotationX)));

            if (ValuesChanged != null){
                ValuesChanged.valuesChanged();
            }
        }
    };

    private SensorEventListener mRotListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // not used
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;

            SensorManager.getRotationMatrixFromVector(rotMatrix, values);

            float[] orientationValues = new float[3];
            SensorManager.getOrientation(rotMatrix, orientationValues);

            setPitch(Graphics.correctAngle((float) Math.toDegrees(orientationValues[1])));
            setRoll(Graphics.correctAngle((float) Math.toDegrees(orientationValues[2])));

            if (ValuesChanged != null){
                ValuesChanged.valuesChanged();
            }
        }
    };
}