package com.example.jaron.accelerometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    int reps = 0;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        reps = getIntent().getIntExtra("Reps", reps);

        result = (TextView)findViewById(R.id.pushup);

        result.setText("" + reps);
    }
}
