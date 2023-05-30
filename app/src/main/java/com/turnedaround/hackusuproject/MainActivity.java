package com.turnedaround.hackusuproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String color = "#C8C8C8";
    String gravity = "Earth";
    boolean portal = false;
    String mazeSize = "Medium";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;



        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("height", height);
            intent.putExtra("width", width);
            intent.putExtra("gravity", gravity);
            intent.putExtra("portal", portal);
            intent.putExtra("color", color);
            intent.putExtra("mazeSize", mazeSize);
            startActivity(intent);
        });


        Button gravButton = findViewById(R.id.grav_button);
        gravButton.setOnClickListener(v->{
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                color=data.getStringExtra("color");
                gravity=data.getStringExtra("gravity");
                portal=data.getBooleanExtra("portal", false);
                mazeSize=data.getStringExtra("mazeSize");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}
