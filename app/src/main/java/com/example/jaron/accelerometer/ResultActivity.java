package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

        WritePointsToUser();

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

    private void WritePointsToUser()
    {
        DocumentReference docRef = db.collection("user").document(currentFireBaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        reps += document.getLong("points").intValue();
                        db.collection("user").document(currentFireBaseUser.getUid()).update(
                                "points", reps
                        );

                    } else {
                    }
                } else {
                }
            }
        });
    }
}
