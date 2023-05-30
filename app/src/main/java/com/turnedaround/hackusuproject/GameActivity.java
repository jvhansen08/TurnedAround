package com.turnedaround.hackusuproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int height = intent.getIntExtra("height", 500);
        int width = intent.getIntExtra("width", 500);
        String gravity = intent.getStringExtra("gravity");
        String color = intent.getStringExtra("color");
        boolean portal = intent.getBooleanExtra("portal", false);
        String mazeSize = intent.getStringExtra("mazeSize");
        gameView = new GameView(this, height, width, gravity, color, portal, mazeSize);
        setContentView(gameView);
    }
}
