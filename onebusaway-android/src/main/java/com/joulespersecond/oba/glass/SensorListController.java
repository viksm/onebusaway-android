package com.joulespersecond.oba.glass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Implements accelerometer-based scrolling of a ListView
 */
public class SensorListController implements SensorEventListener {

    static final String TAG = "SensorListController";

    Context mContext;

    ListView mList;

    ListAdapter mAdapter;

    SensorManager mSensorManager;

    private float[] mRotationMatrix = new float[16];

    private float[] mOrientation = new float[9];

    private float[] history = new float[2];

    private float mHeading;

    private float mPitch;

    public SensorListController(Context context, ListView list, ListAdapter adapter) {
        this.mContext = context;
        this.mList = list;
        mAdapter = adapter;
        history[0] = 10;
        history[1] = 10;
    }

    public void onResume() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mList == null || mAdapter == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            mHeading = (float) Math.toDegrees(mOrientation[0]);
            mPitch = (float) Math.toDegrees(mOrientation[1]);

            float xDelta = history[0] - mHeading;
            float yDelta = history[1] - mPitch;

            history[0] = mHeading;
            history[1] = mPitch;

            float Y_DELTA_THRESHOLD = 0.15f;

            Log.d(TAG, "Y Delta = " + yDelta);

            int scrollHeight = mList.getHeight();

            Log.d(TAG, "ScrollHeight = " + scrollHeight);

            if (yDelta > Y_DELTA_THRESHOLD) {
                Log.d(TAG, "Detected change in pitch up...");
                mList.smoothScrollBy(-scrollHeight, 100);
            } else if (yDelta < -Y_DELTA_THRESHOLD) {
                Log.d(TAG, "Detected change in pitch down...");
                mList.smoothScrollBy(scrollHeight, 100);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
