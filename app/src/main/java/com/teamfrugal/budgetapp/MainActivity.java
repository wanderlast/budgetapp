package com.teamfrugal.budgetapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (FloatingActionButton) findViewById(R.id.camera);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this,
                        cameraActivity.class);
                startActivity(intent);
                /*Intent intent2 = new Intent (MainActivity.this,
                        cameraOCR.class);
                startActivity(intent2);*/
            }
        });
    }
}
