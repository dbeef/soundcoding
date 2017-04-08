package com.dbeef.madpirates.camera;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dbeef.madpirates.auxiliary.Variables;

/**
 * Classic LibGDX camera improved on smooth zooming.
 */
public class ImprovedCamera extends OrthographicCamera {

    private float acceleration;

    public ImprovedCamera(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0;

        this.zoom = Variables.initialZoom;
        update();
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