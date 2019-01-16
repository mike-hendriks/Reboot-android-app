package com.example.jaron.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Situp extends AppCompatActivity  implements SensorEventListener {

    private static final long START_TIME_IN_MILLIS = 60000;

    private TextView situp, SitupTijd;
    private Sensor mySensor;
    private Button stop;
    private SensorManager SM;

    private int i = 0;
    private String workout_id;
    private boolean still_in_range;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        workout_id = getIntent().getStringExtra("workout_id");

        situp = (TextView)findViewById(R.id.situp);
        stop = (Button)findViewById(R.id.stopButton);

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
                WritePointToFirestore();
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
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Situp.this, ResultActivity.class);
                intent.putExtra("Reps", i);
                startActivity(intent);
            }
        });
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
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        SitupTijd.setText(timeLeftFormatted);

        if(minutes == 0 && seconds == 0)
        {
            Intent intent = new Intent(Situp.this, ResultActivity.class);
            intent.putExtra("Reps", i);
            startActivity(intent);
        }
    }

    private void WritePointToFirestore() {
        db.collection("point")
                .whereEqualTo("workout_id", workout_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("point").document(document.getId()).update(
                                        "point", i,
                                        "rep", i
                                );
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Geen workout gevonden met deze code.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
