package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button Deelnemen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Deelnemen = (Button) findViewById(R.id.Deelnemen);
        Deelnemen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenRoom();
            }
        });
    }


    public void OpenRoom() {
        Intent intent = new Intent(this, RoomAddingActivity.class);
        startActivity(intent);
    }
}
