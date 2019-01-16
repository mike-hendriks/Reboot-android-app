package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    int reps = 0;
    long time;
    TextView result, timeLeft;
    ImageButton ExitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reps = getIntent().getIntExtra("Reps", reps);
        time = getIntent().getLongExtra("time", time);

        result = (TextView)findViewById(R.id.pushup);
        timeLeft = (TextView)findViewById(R.id.idJ2);

        result.setText("" + reps);

        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeLeft.setText(timeLeftFormatted);

        ExitResult = (ImageButton) findViewById(R.id.ExitResult);
        ExitResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitResult();
            }
        });
    }

    private void ExitResult() {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
