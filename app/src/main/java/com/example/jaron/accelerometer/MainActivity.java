package com.example.jaron.accelerometer;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("workout");

    private TextView xText, yText, zText, iText, jText;
    private Sensor mySensor;
    private SensorManager SM;
    int i, j = 0;
    private boolean still_in_range, start_pushup = false;
    private Button btnSend, btnSitup, btnPushup, btnOefen, btnLogin, btnRegister;
    final Workout Wo = new Workout();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_main);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView)findViewById(R.id.idX);
        zText = (TextView)findViewById(R.id.idZ);
        yText = (TextView)findViewById(R.id.idY);
        iText = (TextView)findViewById(R.id.idI);
        jText = (TextView)findViewById(R.id.idJ);
        btnSend = (Button)findViewById(R.id.btnAddWorkout);
        btnSitup = (Button)findViewById(R.id.btnSitup);
        btnPushup = (Button)findViewById(R.id.btnPushup);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
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

        //werkt nog niet goed

        if(event.values[2] > 9.9 || start_pushup)
        {
            if(event.values[2] < 9)
            {
                j++;
                jText.setText(String.valueOf(j));
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

    //  CODE voor DATABASE.
    @Override
    protected void onStart() {
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VoegWorkoutToe();
            }
        });

        btnSitup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Situp.class);
                startActivity(intent);
            }
        });

        btnPushup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Pushup .class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
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

        mConditionRef.child("1").child("exercise").child("push_ups").setValue(j);
        mConditionRef.child("1").child("exercise").child("sit_ups").setValue(i);

        Toast.makeText(getApplicationContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();
    }
}
