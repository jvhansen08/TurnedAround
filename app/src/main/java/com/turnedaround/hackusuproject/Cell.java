package com.turnedaround.hackusuproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Arrays;

public class Cell extends GameObject {
    private Boolean[] walls;
    final private float absX;
    final private float absY;
    final private float width;
    final private float height;
    final private int id;
    private Type type = Type.REGULAR;


    public Cell (float absX, float absY, float width, float height, int id) {
        walls = new Boolean[]{true, true, true, true};
        this.absX = absX;
        this.absY = absY;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    // Doesn't really need to be a boolean
    public boolean removeWall(int wall, Cell neighbor) {
        this.walls[wall % 4] = false;
        neighbor.walls[(wall + 2) % 4] = false;
        return true;
    }

    // Now a duplicate with removeWall. Consider removing
    public void unsafeRemoveWall(int wall, Cell neighbor) {
        this.walls[wall % 4] = false;
        neighbor.walls[(wall + 2) % 4] = false;
    }

    public void reset() {
        Arrays.fill(walls, true);
        type = Type.REGULAR;
    }

    /**
     * getter for walls array
     * @return boolean[] = {UP, RIGHT, DOWN, LEFT}
     */
    public Boolean[] getWalls() {
        return walls;
    }

    public Boolean getUpWall() {
        return walls[0];
    }

    public Boolean getRightWall() {
        return walls[1];
    }

    public Boolean getDownWall() {
        return walls[2];
    }

    public Boolean getLeftWall() {
        return walls[3];
    }


    public int getId() {
        return id;
    }

    public float[] getPos() {
        return new float[]{absX, absY};
    }



    // Sam, Don't forget to put the canvas back where it goes if you move it.
    //                                  -Sam
    @Override
    public void render(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(absX,absY);
        paint.reset();
        if(type == Type.END) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawOval(width * .1f,
                    height * .7f,
                    width * .9f,
                    height *.9f,
                    paint);
            paint.setStrokeWidth(width * .1f);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(0xffaa0000);
            canvas.drawLine(width * .5f, height * .25f, width * .9f, height *.3f ,paint);
            canvas.drawLine(width * .5f, height * .35f, width * .9f, height *.3f ,paint);
            paint.setColor(Color.RED);
            canvas.drawLine(width * .5f, height * .15f, width * .9f, height *.3f ,paint);
            canvas.drawLine(width * .5f, height * .45f, width * .9f, height *.3f ,paint);


            paint.setColor(0xffcccccc);
            canvas.drawLine(width * .5f, height * .8f, width * .5f, height * .2f, paint);

            paint.setStrokeWidth(width * .15f);
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(width * .5f, height * .125f, width *.1f, paint);
        }
        if(type == Type.PORTALA || type == Type.PORTALB) {
            float radius = width * .4f;
            while (radius >= 1) {
                paint.setColor(0xff1fc2bc);
//            paint.setColor(0xff0055ff);
                canvas.drawCircle(width * .5f, height * .5f, radius, paint);
                radius *= .8;

//            paint.setColor(0xff6666ff);
                paint.setColor(0xff1f83c2);
                canvas.drawCircle(width * .5f, height * .5f, radius, paint);
                radius *= .8;
            }
        }
        // for debuging, uncomment to show path
//        if (type == Type.PATH) {
//            canvas.drawRect(2* width / 5, 2 * width / 5, 3 * width / 5, 3 * height / 5, paint);
//        }

        paint.setStrokeWidth(15);
        if (walls[1]) {
//            paint.setStrokeCap(Paint.Cap.BUTT);
//            paint.setColor(0x66000000);
//            canvas.drawLine(width+7, 10, width+7, height+10, paint);

            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(0xff303002);
            canvas.drawLine(width, 0, width, height, paint);
        }
        if (walls[2]) {
//            paint.setStrokeCap(Paint.Cap.BUTT);
//            paint.setColor(0x66000000);
//            canvas.drawLine(7, height+10, width+7, height+10, paint);

            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(0xff303002);
            canvas.drawLine(0, height, width, height, paint);
        }
        paint.reset();
        canvas.restore();
    }

    public void setType(Type type) {
        this.type = type;
    }

    enum Type{
        REGULAR,
        END,
        PORTALA,
        PORTALB,
        PATH;
    }

    // For debugging purposes
    public String toString() {
        return id + "";
    }
}
