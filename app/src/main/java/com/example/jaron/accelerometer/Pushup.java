package com.example.jaron.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pushup extends AppCompatActivity  implements SensorEventListener{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("workout");

    private TextView pushup;
    private Sensor mySensor;
    private SensorManager SM;
    int j = 0;
    private boolean start_pushup = false;
    //final Workout Wo = new Workout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_pushup);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        pushup = (TextView)findViewById(R.id.pushup);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.values[2] > 9.9 || start_pushup)
        {
            if(event.values[2] < 9)
            {
                j++;
                pushup.setText(String.valueOf(j));
                mConditionRef.child("1").child("exercise").child("push_ups").setValue(j);
                start_pushup = false;
            }
            else
            {
                start_pushup = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VoegWorkoutToe();
            }
        });*/
    }

    public void VoegWorkoutToe()
    {
        /*Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");

        Wo.setId("3");
        Wo.setDate(mdformat.format(calendar.getTime()));
        Wo.setExercise("");

        Workout workout = new Workout(Wo.getId(), Wo.getDate(), Wo.getExercise());
        mConditionRef.child("1").setValue(workout);

        mConditionRef.child("1").child("exercise").child("push_ups").setValue(j);

        Toast.makeText(getApplicationContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();*/
    }
}
