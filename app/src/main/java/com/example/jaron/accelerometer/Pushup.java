package com.example.jaron.accelerometer;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pushup extends AppCompatActivity  implements SensorEventListener{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("workout");

    private static final long START_TIME_IN_MILLIS = 60000;
    private static boolean sensorUpdateEnabled = false;
    private TextView pushup, PushupTijd;
    private Button stop;
    private Sensor mySensor;
    private SensorManager SM;
    int j = 0;
    private boolean start_pushup = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CountDownTimer mCountDownTimer;
    String point_id;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

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

        point_id = getIntent().getStringExtra("point_id");

        pushup = (TextView)findViewById(R.id.pushup);
        stop = (Button)findViewById(R.id.stopButton);

        PushupTijd = findViewById(R.id.Pushuptijd);
        startTimer();
        updateCountDownText();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!sensorUpdateEnabled)
        {
            SM.unregisterListener(this, mySensor);
        }

        if(event.values[2] > 9.9 || start_pushup)
        {
            if(event.values[2] < 9)
            {
                j++;
                pushup.setText(String.valueOf(j));
                WritePointToFirestore();
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
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pushup.this, ResultActivity.class);
                intent.putExtra("Reps", j);
                finish();
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

        PushupTijd.setText(timeLeftFormatted);

        if(minutes == 0 && seconds == 0)
        {
            Intent intent = new Intent(Pushup.this, ResultActivity.class);
            intent.putExtra("Reps", j);
            finish();
            startActivity(intent);
        }
    }

    private void WritePointToFirestore() {
        db.collection("point").document(point_id).update(
                "point", j,
                "rep", j
        );
    }
}
