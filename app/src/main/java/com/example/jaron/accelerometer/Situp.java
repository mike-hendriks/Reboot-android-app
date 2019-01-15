package com.example.jaron.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Situp extends AppCompatActivity  implements SensorEventListener {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("workout");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final long START_TIME_IN_MILLIS = 60000;
    private TextView situp, SitupTijd;
    private Sensor mySensor;
    private SensorManager SM;
    int i = 0;
    private boolean still_in_range;
    //final Workout Wo = new Workout();*/

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_situp);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        situp = (TextView)findViewById(R.id.situp);

        SitupTijd = findViewById(R.id.SitupTijd);
        startTimer();
        updateCountDownText();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.values[1] > 9){

            if(still_in_range == false) {
                i++;
                situp.setText(String.valueOf(i));
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
        /*btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VoegWorkoutToe();
            }
        });*/
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                mTimerRunning = false;
                WritePointToFirestore();
                WriteMessage();
                Intent intent = new Intent(Situp.this, ResultActivity.class);
                startActivity(intent);
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        SitupTijd.setText(timeLeftFormatted);
    }

    private void WritePointToFirestore()
    {

        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> point = new HashMap<>();
        point.put("exercise_id", "Sit up");
        point.put("point", i);
        point.put("rep", i);
        point.put("user_id", currentFireBaseUser.getUid());
        point.put("workout_id", "234");

        db.collection("point").document()
                .set(point)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    public void WriteMessage(){

        String Message = situp.getText().toString();
        String file_name = "hello_file";
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            fileOutputStream.write(Message.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        mConditionRef.child("1").child("exercise").child("sit_ups").setValue(i);

        Toast.makeText(getApplicationContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();*/
    }
}
