package com.example.wearos;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    TextView textView_X;
    TextView textView_Y;
    TextView textView_Z;
    ImageView imgView;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener eventListener;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView_X = findViewById(R.id.txtX);
        textView_Y = findViewById(R.id.txtY);
        textView_Z = findViewById(R.id.txtZ);
        imgView = findViewById(R.id.img);

        //Обращение к системному вызову с проверкой
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            Log.d("AAA", "hell");
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        Log.d("AAA", "hello");
        eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.d("AAA", "hello2");
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y, remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }
                textView_X.setText(String.format("X=%s", String.valueOf(((int) orientations[1]))));
                textView_Y.setText(String.format("Y=%s", String.valueOf(((int) orientations[2]))));
                textView_Z.setText(String.format("Z=%s", String.valueOf(((int) orientations[0]))));
                imgView.setRotation(orientations[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    //Регистрация прослушивания системного менеджера
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(eventListener,sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //Отмена регистрации системного менеджера
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(eventListener);
    }
}