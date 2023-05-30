package com.turnedaround.hackusuproject;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball extends GameObject{
    private float[] pos = new float[2]; //[x, y]
    private float radius;
    private float[] velocity = {0, 0}; //[x, y]
    private String color = "#C8C8C8";
    private float[] cellDimensions = new float[2];
    private String gravityText = "Earth";
    private float gravity;
    private Maze maze;

    private final int GRAVITYSCALE = 5;
    private float[] tilt = new float[]{0,0,0};

    public Ball(float[] position, Maze maze, String color, String gravityText){
        this.cellDimensions[0] = maze.getCellSize()[0];
        this.cellDimensions[1] = maze.getCellSize()[1];
        this.radius = (2 * (this.cellDimensions[0]) / 6);
        this.pos = position;
        this.pos[0] += maze.getCellSize()[0]/2;
        this.pos[1] += maze.getCellSize()[1]/2;
        this.color = color;
        this.maze = maze;
        if(gravityText.equals("Earth")){
            this.gravity = 9.8f;
        }else if(gravityText.equals("Jupiter")){
            this.gravity = 23.12f;
        }else if(gravityText.equals("Moon")){
            this.gravity = 1.6f;
        }else if(gravityText.equals("Pluto")){
            this.gravity = .6f;
        }else{
            this.gravity = 9.8f;
        }
    }


    @Override
    public void render(Canvas canvas, Paint paint){
        // drop shadow on ball
        paint.setColor(0x55303002);
//        canvas.drawCircle(pos[0] + radius * (float)Math.sin(tilt[2]), pos[1] - radius * (float)Math.sin(tilt[1]), radius, paint);

        canvas.save();
        canvas.translate(pos[0], pos[1]);
        canvas.rotate(-(float)Math.toDegrees(Math.atan2(sin(tilt[1]),sin(tilt[2]))));
        tilt[1] *= 2;
        tilt[2] *= 2;
        float opp = (float)Math.sqrt(sin(tilt[1])*sin(tilt[1])+sin(tilt[2]) * sin(tilt[2]));
        float hyp = (float)Math.sqrt(2+2*cos(tilt[1]) * cos(tilt[2]));
        float tiltAngle = (float)(Math.asin(opp/hyp));
        canvas.drawOval(
                (float) (radius * (Math.sin(tiltAngle) + 1) / Math.cos(tiltAngle)),
                -radius,
                (float) (radius * (Math.sin(tiltAngle) - 1) / Math.cos(tiltAngle)),
                radius,
                paint);
        canvas.restore();

        paint.setColor(Color.parseColor(this.color));
        canvas.drawCircle(pos[0], pos[1], radius, paint);
        paint.reset();
    }

    public String update(float[] rotationVector, float elapsedTime){
        tilt = rotationVector;
        // Try to split update in half if velocity is too great
        if (Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]) * elapsedTime / radius > .8){
            float time = elapsedTime / 2;
            update(rotationVector, time);
            return(update(rotationVector, time));
        }

        velocity[0] += GRAVITYSCALE * gravity * (float)Math.sin(rotationVector[2]);

        velocity[1] -= GRAVITYSCALE * gravity * (float)Math.sin(rotationVector[1]);

        pos[0] = pos[0] + (velocity[0] * elapsedTime);
        pos[1] = pos[1] + (velocity[1] * elapsedTime);

        Cell cell = maze.getCellAt((int) (pos[0]/cellDimensions[0]), (int) (pos[1]/cellDimensions[1]));

        if (cell.getLeftWall() && (pos[0] - radius) - 5 <= cell.getPos()[0]){
            velocity[0] = 0;
            pos[0] = cell.getPos()[0] + radius + 5;
        }
        if (cell.getRightWall() && (pos[0] + radius) + 5 >= (cell.getPos()[0] + cellDimensions[0])){
            velocity[0] = 0;
            pos[0] = (cell.getPos()[0] + cellDimensions[0]) - radius - 5;
        }
        if (cell.getUpWall() && (pos[1] - radius) - 5 <= cell.getPos()[1]){
            velocity[1] = 0;
            pos[1] = (cell.getPos()[1] + radius) + 5;
        }
        if (cell.getDownWall() && (pos[1] + radius) + 5 >= (cell.getPos()[1] + cellDimensions[1])){
            velocity[1] = 0;
            pos[1] = (cell.getPos()[1] + cellDimensions[1]) - radius - 5;
        }

        float[][] corners = new float[4][];
        corners[0] = cell.getPos();
        corners[1] = new float[]{corners[0][0] + cellDimensions[0], corners[0][1]};
        corners[2] = new float[]{corners[0][0], corners[0][1] + cellDimensions[1]};
        corners[3] = new float[]{corners[0][0] + cellDimensions[0], corners[0][1] + cellDimensions[1]};



        for (float[] c : corners) {
            float d = (float)Math.sqrt((pos[0] - c[0]) * (pos[0] - c[0]) + (pos[1] - c[1]) * (pos[1] - c[1])) - 5;
            if (d < radius) {
                pos[0] += (radius - d) * (pos[0]-c[0])/d;
                pos[1] += (radius - d) * (pos[1]-c[1])/d;
            }
        }

        if (maze.portalA != null){
            float diffXPortalA = Math.abs(this.pos[0] - (maze.portalA.getPos()[0] + (this.cellDimensions[0] / 2)));
            float diffYPortalA = Math.abs(this.pos[1] - (maze.portalA.getPos()[1] + (this.cellDimensions[1] / 2)));
            float distPortalA = (float) Math.sqrt((diffXPortalA * diffXPortalA) + (diffYPortalA * diffYPortalA));

            float diffXPortalB = Math.abs(this.pos[0] - (maze.portalB.getPos()[0] + (this.cellDimensions[0] / 2)));
            float diffYPortalB = Math.abs(this.pos[1] - (maze.portalB.getPos()[1] + (this.cellDimensions[1] / 2)));
            float distPortalB = (float) Math.sqrt((diffXPortalB * diffXPortalB) + (diffYPortalB * diffYPortalB));

            if (distPortalA >= cellDimensions[1] &&
                    distPortalB >= cellDimensions[1]) {
                maze.portalA.setType(Cell.Type.PORTALA);
                maze.portalB.setType(Cell.Type.PORTALB);
            }
        }

        if(cell.getType() == Cell.Type.END){
            return "END";
        }
        else if (cell.getType() == Cell.Type.PORTALA){
            return "GOTOB";
        }
        else if (cell.getType() == Cell.Type.PORTALB){
            return "GOTOA";
        }
        return "NORMAL";
    }

    public Cell getCurrCell(){
        return maze.getCellAt((int) (pos[0]/cellDimensions[0]), (int) (pos[1]/cellDimensions[1]));
    }

    public void setNewLocation(float[] coords){
        pos[0] = coords[0];
        pos[1] = coords[1];
    }

    public float getRadius(){
        return this.radius;
    }
}
