package com.example.jaron.accelerometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResultActivity extends AppCompatActivity {
    int reps = 0;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reps = getIntent().getIntExtra("Reps", reps);

        result = (TextView)findViewById(R.id.pushup);
        result.setText("" + reps);
    }
}
