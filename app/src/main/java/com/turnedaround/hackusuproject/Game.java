package com.turnedaround.hackusuproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.graphics.Color;


public class Game extends View {
    private final Maze maze;
    long time = System.nanoTime();
    boolean gameOver = false;
    int WIDTH;
    int HEIGHT;
    float screenHeight;
    float screenWidth;
    String gravity;
    String color;
    String mazeSize;
    boolean portal;

    private final Ball ball;

    public Game(Context context, float screenHeight, float screenWidth, String gravity, String color, boolean portal, String mazeSize) {
        super(context);
        System.out.println(mazeSize);
        if (mazeSize.equals("small")) {
            this.WIDTH = 8;
            this.HEIGHT = 16;
        } else if (mazeSize.equals("Medium")) {
            this.WIDTH = 10;
            this.HEIGHT = 20;
        } else if (mazeSize.equals("LARGE")) {
            this.WIDTH = 15;
            this.HEIGHT = 30;
        } else if (mazeSize.equals("tiny")) {
            this.WIDTH = 4;
            this.HEIGHT = 8;
        }else{
            this.WIDTH=10;
            this.HEIGHT=20;
        }
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maze = new Maze(WIDTH, HEIGHT, screenHeight, screenWidth, portal);
        this.ball = new Ball(maze.start.getPos(), maze, color, gravity);
        this.gravity = gravity;
        this.portal = portal;
        this.color = color;
        this.mazeSize = mazeSize;
    }


    public void draw(Canvas canvas, Paint paint) {
        maze.render(canvas, paint);
        ball.render(canvas, paint);

        if(gameOver){
            paint.setARGB(75, 0, 0, 0);
            canvas.drawRect(0, screenHeight/4, screenWidth, (float)(screenHeight*0.69), paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(130);
            canvas.drawText("A-MAZE-ING!", (screenWidth/2)-380, (screenHeight/2 - 160), paint);
            paint.setTextSize(220);
            canvas.drawText("YOU WIN", (screenWidth/2)-440, (screenHeight/2+100), paint);
        }
    }

    public void handleChange(SensorEvent event){
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        float[] values = new float[3];
        SensorManager.getOrientation(rotationMatrix, values);
        // I got tired of converting back to degrees for trig functions, so we're in radians now
//        for (int i=0; i<3; i++){
//            values[i] = (float) Math.toDegrees(values[i]);
//        }
        float timeElapsed = System.nanoTime() - time;
        time = System.nanoTime();
        String ballStatus = ball.update(values, timeElapsed /1000000000f );
        if (ballStatus.equals("END")){
            gameOver = true;
        }
        else if(ballStatus.equals("GOTOA")){
            Cell portal = maze.portalA;
            float[] coords =  portal.getPos();
            coords[0] += ball.getRadius();
            coords[1] += ball.getRadius();
            ball.getCurrCell().setType(Cell.Type.REGULAR);
            ball.setNewLocation(coords);
            ball.getCurrCell().setType(Cell.Type.REGULAR);
        }
        else if (ballStatus.equals("GOTOB")){
            Cell portal = maze.portalB;
            float[] coords =  portal.getPos();
            coords[0] += ball.getRadius();
            coords[1] += ball.getRadius();
            ball.getCurrCell().setType(Cell.Type.REGULAR);
            ball.setNewLocation(coords);
            ball.getCurrCell().setType(Cell.Type.REGULAR);
        }
    }
}
