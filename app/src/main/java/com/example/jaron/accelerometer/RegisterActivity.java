package com.example.jaron.accelerometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword, editTextGebruikersnaam;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.Email);
        editTextPassword = findViewById(R.id.Wachtwoord);
        editTextGebruikersnaam = findViewById(R.id.Gebruikersnaam);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.Registeren).setOnClickListener(this);
    }

    private void RegistreerUser () {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String gebruikersnaam = editTextGebruikersnaam.getText().toString().trim();

        if (gebruikersnaam.isEmpty())
        {
            editTextGebruikersnaam.setError("Gebruikersnaam is nodig");
            editTextGebruikersnaam.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editTextEmail.setError("E-mail is nodig");
            editTextPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Vul een goed email adres in");
            editTextPassword.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Wachtwoord is nodig");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            editTextPassword.setError("Minimale lengte van het wachtwoord moet 6 zijn");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Gelukt", Toast.LENGTH_SHORT).show();
                    SchrijfUserNaarFireStore();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{

                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Dit Email adres is al in gebruik", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void SchrijfUserNaarFireStore()
    {
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> user = new HashMap<>();
        user.put("email", currentFireBaseUser.getEmail());
        user.put("fullname", editTextGebruikersnaam.getText().toString().trim());
        user.put("points", 0);

        db.collection("user").document(currentFireBaseUser.getUid())
                .set(user)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Registeren:
                RegistreerUser();
                break;
        }
    }
}