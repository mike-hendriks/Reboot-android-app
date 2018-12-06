package com.example.jaron.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView xText, yText, zText, iText;
    private Sensor mySensor;
    private SensorManager SM;
    int i = 0;
    private boolean still_in_range = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView)findViewById(R.id.idX);
        zText = (TextView)findViewById(R.id.idZ);
        yText = (TextView)findViewById(R.id.idY);
        iText = (TextView)findViewById(R.id.idI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);

        if(event.values[1] > 9){

            if(still_in_range == false) {
                i++;
                iText.setText("Sit ups: " + i);
                still_in_range = true;
            }
        }else {
            still_in_range = false;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
