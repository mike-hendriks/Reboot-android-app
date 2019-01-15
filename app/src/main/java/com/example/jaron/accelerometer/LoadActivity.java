package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoadActivity extends AppCompatActivity {

    private Button Cancel, Doorgaan;
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

        Doorgaan = (Button) findViewById(R.id.Doorgaan);
        Doorgaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDoorgaan();
            }
        });

        //WritePointToFirestore();
    }

    private void btnCancel() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void btnDoorgaan() {
        Intent intent = new Intent(this, Situp.class);
        startActivity(intent);
    }

//    private void WritePointToFirestore()
//    {
//        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        Map<String, Object> point = new HashMap<>();
//        point.put("exercise_id", "push ups");
//        point.put("point", 10);
//        point.put("rep", 10);
//        point.put("user_id", currentFireBaseUser.getUid());
//        point.put("workout_id", "234");
//
//        db.collection("point").document()
//                .set(point)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//    }


}
