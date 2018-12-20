package com.example.jaron.accelerometer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class LoginActivity extends AppCompatActivity {
    //FrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_login);

        //framelayout = (FrameLayout)findViewById(R.id.frameContainer);
        //framelayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }


}
