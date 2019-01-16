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

    private static boolean sensorUpdateEnabled;

    private TextView situp, SitupTijd;
    private Sensor mySensor;
    private Button stop;
    private SensorManager SM;

    private int i = 0;
    private int time = 0;
    private String point_id;
    private boolean still_in_range;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis, mTimeStart;

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

        point_id = getIntent().getStringExtra("point_id");

        situp = (TextView)findViewById(R.id.situp);
        stop = (Button)findViewById(R.id.stopButton);

        time = getIntent().getIntExtra("time", time);
        mTimeStart = time * 1000;
        mTimeLeftInMillis = mTimeStart;

        SitupTijd = findViewById(R.id.SitupTijd);

        sensorUpdateEnabled = true;

        startTimer();
        updateCountDownText();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!sensorUpdateEnabled)
        {
            SM.unregisterListener(this, mySensor);
        }

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
                intent.putExtra("time", mTimeStart - mTimeLeftInMillis);

                mCountDownTimer.cancel();
                mCountDownTimer = null;
                sensorUpdateEnabled = false;

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

        SitupTijd.setText(timeLeftFormatted);

        if(minutes == 0 && seconds == 0)
        {
            Intent intent = new Intent(Situp.this, ResultActivity.class);
            intent.putExtra("Reps", i);
            intent.putExtra("time", mTimeStart);
            mTimerRunning = false;
            sensorUpdateEnabled = false;
            finish();
            startActivity(intent);
        }
    }

    private void WritePointToFirestore() {
        db.collection("point").document(point_id).update(
                "point", i,
                "rep", i
        );
    }
}
