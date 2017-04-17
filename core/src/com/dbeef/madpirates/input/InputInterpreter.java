package com.dbeef.madpirates.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * From: https://github.com/libgdx/libgdx/wiki/Gesture-detection
 */
public class InputInterpreter implements GestureListener {

    public String message = "No data yet";
    public boolean touched;
    Vector3 xyzTap = new Vector3();
    double xTap;
    double yTap;
    double zoomDelta;
    boolean deltaChanged;
    public InputInterpreter() {
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    public boolean isTouched() {
    return touched;
    }

    public double getZoomDelta() {
        if (deltaChanged == true) {
            deltaChanged = false;
            return zoomDelta / 100;
        } else
            return 0;


    }

    public double getXTap() {
        touched = false;
        return xTap;
    }

    public double getYTap() {
        touched = false;
        return yTap;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        System.out.println("touchDown");
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        System.out.println("tap");
        touched = true;
        xTap = x;
        yTap = y;
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        System.out.println("longPress");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // TODO Auto-generated method stub
        System.out.println("fling");
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO Auto-generated method stub
        System.out.println("pan");
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        System.out.println("panStop");
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        System.out.println("zoom");
        System.out.println("initialDistance: " + initialDistance + "distance: " + distance);
        message = "initialDistance: " + initialDistance + "distance: " + distance;

        if (initialDistance > distance)
            zoomDelta = initialDistance / distance;
        else if (initialDistance < distance)
            zoomDelta = distance / initialDistance;
        if (distance > initialDistance) {
            zoomDelta *= (-1);
        }
        deltaChanged = true;


        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        System.out.println("pinch");
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public Vector3 getLastTouchPosition() {
        xyzTap.x = (float) xTap;
        xyzTap.y = (float) yTap;
        return xyzTap;
    }
}