package com.turnedaround.hackusuproject;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    public void update(long timePassed){}

    public abstract void render(Canvas canvas, Paint paint);
}
