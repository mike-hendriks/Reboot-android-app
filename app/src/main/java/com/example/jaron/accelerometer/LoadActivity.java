package com.example.jaron.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadActivity extends AppCompatActivity {

    private Button Cancel;

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
    }

    private void btnCancel() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
