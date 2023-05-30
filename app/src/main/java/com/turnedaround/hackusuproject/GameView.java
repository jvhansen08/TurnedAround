package com.turnedaround.hackusuproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

public class GameView extends View {
    Paint paint = new Paint();
    Game game;

    public GameView(Context context, int height, int width, String gravity, String color, boolean portal, String mazeSize){
        super(context);
        game = new Game(getContext(), height, width, gravity, color, portal, mazeSize);
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        SensorEventListener rotationVectorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                game.handleChange(event);
                invalidate();
                if(!game.gameOver){
                    invalidate();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(rotationVectorListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDraw(Canvas canvas){
        game.draw(canvas, paint);
    }
}

