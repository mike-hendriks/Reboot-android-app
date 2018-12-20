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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Situp extends AppCompatActivity  implements SensorEventListener {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("workout");

    private TextView xText, yText, zText, iText;
    private Sensor mySensor;
    private SensorManager SM;
    int i = 0;
    private boolean still_in_range;
    private Button btnSend;
    final Workout Wo = new Workout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situp);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView)findViewById(R.id.idX);
        zText = (TextView)findViewById(R.id.idZ);
        yText = (TextView)findViewById(R.id.idY);
        iText = (TextView)findViewById(R.id.idI);
        btnSend = (Button)findViewById(R.id.btnAddWorkout);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xText.setText("X: " + event.values[0]);
        yText.setText("Y: " + event.values[1]);
        zText.setText("Z: " + event.values[2]);

        if(event.values[1] > 9){

            if(still_in_range == false) {
                i++;
                iText.setText(String.valueOf(i));
                mConditionRef.child("1").child("exercise").child("sit_ups").setValue(i);
                still_in_range = true;
            }
        }else {
            still_in_range = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VoegWorkoutToe();
            }
        });
    }

    public void VoegWorkoutToe()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");

        Wo.setId("3");
        Wo.setDate(mdformat.format(calendar.getTime()));
        Wo.setExercise("");

        Workout workout = new Workout(Wo.getId(), Wo.getDate(), Wo.getExercise());
        mConditionRef.child("1").setValue(workout);

        mConditionRef.child("1").child("exercise").child("sit_ups").setValue(i);

        Toast.makeText(getApplicationContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();
    }
}
