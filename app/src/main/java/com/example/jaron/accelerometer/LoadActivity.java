package com.example.jaron.accelerometer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class LoadActivity extends AppCompatActivity {

    private Button Cancel;
    String workout_id, exercise;
    int time;
    DocumentReference ref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Cancel = (Button) findViewById(R.id.btncancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel();
            }
        });

        workout_id = getIntent().getStringExtra("workout_id");

        WritePointToFirestore();
    }

    private void btnCancel() {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private void WritePointToFirestore()
    {
        db.collection("workout_exercise")
                .whereEqualTo("workout_id", workout_id) //"GT7IU1jxAfdONexQfasQ")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                exercise = document.getString("exercise_id");
                                FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                Map<String, Object> point = new HashMap<>();
                                point.put("exercise_id", exercise);
                                point.put("point", 0);
                                point.put("rep", 0);
                                point.put("user_id", currentFireBaseUser.getUid());
                                point.put("workout_id", workout_id);

                                ref = db.collection("point").document();
                                        ref.set(point)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                DocumentReference docRef = db.collection("workout").document(workout_id);
                                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                        if(documentSnapshot.getDouble("start") == 1)
                                                        {
                                                            if(exercise.equals("pushups"))
                                                            {
                                                                DocumentReference docRef = db.collection("exercise").document("pushups");
                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                time = document.getLong("time").intValue();
                                                                                Intent intent = new Intent(LoadActivity.this, Pushup.class);
                                                                                intent.putExtra("point_id", ref.getId());
                                                                                intent.putExtra("time", time);
                                                                                finish();
                                                                                startActivity(intent);

                                                                            } else {
                                                                            }
                                                                        } else {
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                            else if(exercise.equals("situps"))
                                                            {
                                                                DocumentReference docRef = db.collection("exercise").document("situps");
                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                time = document.getLong("time").intValue();
                                                                                Intent intent = new Intent(LoadActivity.this, Situp.class);
                                                                                intent.putExtra("point_id", ref.getId());
                                                                                intent.putExtra("time", time);
                                                                                finish();
                                                                                startActivity(intent);

                                                                            } else {
                                                                            }
                                                                        } else {
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Geen workout gevonden met deze code.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}



