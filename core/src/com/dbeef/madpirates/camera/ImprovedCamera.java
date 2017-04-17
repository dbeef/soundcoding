package com.dbeef.madpirates.camera;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dbeef.madpirates.auxiliary.Variables;

/**
 * Classic LibGDX camera improved on smooth zooming.
 */
public class ImprovedCamera extends OrthographicCamera {
    private int max_x;
    private int max_y;
    private int min_x;
    private int min_y;
    private float acceleration;

    public ImprovedCamera(float viewportWidth, float viewportHeight, int max_x, int max_y, int min_x, int min_y) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0;
        this.max_x = max_x;
        this.max_y = max_y;
        this.min_x = min_x;
        this.min_y = min_y;
        this.zoom = Variables.initialZoom;
        update();
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public int getMin_x() {
        return min_x;
    }

    public void setMin_x(int min_x) {
        this.min_x = min_x;
    }

    public int getMin_y() {
        return min_y;
    }

    public void setMin_y(int min_y) {
        this.min_y = min_y;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        if (this.position.x > max_x) {
            position.x = max_x;
        }
        if (this.position.x < min_x) {
            position.x = min_x;
        }
        if (this.position.y > max_y) {
            this.position.y = max_y;
        }
        if (this.position.y < min_y) {
            this.position.y = min_y;
        }
    }

    public void takeZoomDelta(double deltaZoom) {
        acceleration += deltaZoom;
        if (acceleration > 0)
            acceleration -= Gdx.graphics.getDeltaTime() * 0.2f;
        if (acceleration < 0)
            acceleration += Gdx.graphics.getDeltaTime() * 0.2f;

        this.zoom += acceleration;

        if (this.zoom > Variables.maxZoom) {
            this.zoom = Variables.maxZoom;
            acceleration = 0;
        }
        if (this.zoom < Variables.minZoom) {
            this.zoom = Variables.minZoom;
            acceleration = 0;
        }
    }
}