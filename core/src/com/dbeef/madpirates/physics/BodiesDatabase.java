package com.dbeef.madpirates.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BodiesDatabase extends Array<Body> {

    public static final float FPSCAP = 1 / 60F;
    float shipSailingSpeed;
    float shipManouverSpeed;
    float destinatedX;
    float destinatedY;
    float destinatedAngle;
    float pointsX;
    float pointsY;
    boolean loaded = false;
    World world;
    private float accumulator = 0;

    public BodiesDatabase() {
        this(true, 16);
    }

    public BodiesDatabase(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = (Body[]) new Body[capacity];
        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        world = new World(new Vector2(0, 0f), true);


    }

    public float getShipSailingSpeed() {
        return shipSailingSpeed;
    }

    public void setShipSailingSpeed(float shipSailingSpeed) {
        this.shipSailingSpeed = shipSailingSpeed;
        if (shipSailingSpeed > 20.7)
            shipSailingSpeed = 20.7f;
        if (shipSailingSpeed < 0)
            shipSailingSpeed = 0f;
    }

    public int add(Body value, BodyDef bodyDef, FixtureDef fixtureDef) {
        Body b;
        b = world.createBody(bodyDef);
        b.createFixture(fixtureDef);

        Body[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size++] = b;

        if (loaded == false) {
        }
        return this.size - 1;
    }

    public void simulate(float deltaTime) {
        //How does it work?
        //http://saltares.com/blog/games/fixing-your-timestep-in-libgdx-and-box2d/

        accumulator += deltaTime;
        while (accumulator > FPSCAP) {
            world.step(FPSCAP, 6, 2);
            accumulator -= FPSCAP;

            float bodyAngle = this.get(0).getAngle();

            float linearVelocityX = (float) Math.cos(bodyAngle + 1.45f) * shipSailingSpeed;
            float linearVelocityY = (float) Math.sin(bodyAngle + 1.45f) * shipSailingSpeed;
            Vector2 linearVelocity = this.get(0).getLinearVelocity();


            if (bodyAngle < destinatedAngle) {
                this.get(0).setAngularVelocity(0.55f);
                this.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);
            }

            if (bodyAngle > destinatedAngle - 0.1f && bodyAngle < destinatedAngle + 0.1f) {
                this.get(0).setAngularVelocity(0f);
                //		this.get(0).setLinearVelocity(0,0);
            }

            if (bodyAngle > destinatedAngle) {
                this.get(0).setAngularVelocity(-1 * 0.55f);
                this.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);

            }
            if (bodyAngle < destinatedAngle - 0.1f && bodyAngle > destinatedAngle + 0.1f) {
                this.get(0).setAngularVelocity(0f);
                //		this.get(0).setLinearVelocity(0,0);
            }

        }
    }

    public float sideToRotate(float mouseX, float mouseY) {

        //Trigonometry, we've got mouse position and ship position
        //We want an angle between X axis and mouse click location

        Vector2 clickedPoint = new Vector2();
        clickedPoint.set((int) mouseX, (int) mouseY);
        Vector2 toTarget = new Vector2();
        toTarget.x = clickedPoint.x - this.get(0).getPosition().x;
        toTarget.y = clickedPoint.y - this.get(0).getPosition().y;

        destinatedAngle = MathUtils.atan2(-toTarget.x, toTarget.y);

        destinatedX = mouseX;
        destinatedY = mouseY;


        pointsX = destinatedX - this.get(0).getPosition().x;
        pointsY = destinatedY - this.get(0).getPosition().y;

//Now we've got destinatedAngle in radians

        return ((float) destinatedAngle);
    }

    public void getShipCharacteristics(float sS, float mS) {
        shipSailingSpeed = sS;
        shipManouverSpeed = mS;
    }

    public void dispose() {
        world.dispose();
    }

    public World getWorld() {
        return world;
    }
}