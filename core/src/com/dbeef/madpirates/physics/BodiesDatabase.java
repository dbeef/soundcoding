package com.dbeef.madpirates.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dbeef.madpirates.models.Dot;

import java.util.ArrayList;

public class BodiesDatabase {

    public static final float FPSCAP = 1 / 60F;
    float shipSailingSpeed;
    float shipManouverSpeed;
    float destinatedX;
    float destinatedY;
    float destinatedAngle;
    float pointsX;
    float pointsY;
    float timerDot = 0;
    boolean loaded = false;
    World world;
    World alternativeWorld;
    ArrayList<Body> alternativeBodies;
    private ArrayList<Dot> dots;
    private ArrayList<Body> bodies;
    private ArrayList<BodyDef> bodyDefs;
    private ArrayList<FixtureDef> fixtureDefs;
    private float accumulator = 0;
    private float alternateAccumulator = 0;
    private float alternativeWorldTimer = 0;
    public BodiesDatabase() {
        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        world = new World(new Vector2(0, 0f), false);
        world.setWarmStarting(true);
        world.setContinuousPhysics(true);

        bodies = new ArrayList<Body>();
        bodyDefs = new ArrayList<BodyDef>();
        fixtureDefs = new ArrayList<FixtureDef>();
        dots = new ArrayList<Dot>();

        alternativeBodies = new ArrayList<Body>();
        alternativeWorld = new World(new Vector2(0, 0f), false);
    }

    public World getAlternativeWorld() {
        return alternativeWorld;
    }

    public void setAlternativeWorld(World alternativeWorld) {
        this.alternativeWorld = alternativeWorld;
    }

    public ArrayList<Dot> getDots() {
        return dots;
    }

    public void captureSimulation() {
        dots = new ArrayList<Dot>();
        for (int a = 0; a < alternativeBodies.size(); a++) {
            alternativeBodies.get(a).setLinearVelocity(bodies.get(a).getLinearVelocity());
            alternativeBodies.get(a).setAngularVelocity(bodies.get(a).getAngularVelocity());
            alternativeBodies.get(a).setTransform(bodies.get(a).getTransform().getPosition(), bodies.get(a).getAngle());
            alternativeBodies.get(a).setActive(true);
            alternativeBodies.get(a).setAwake(true);
            alternativeBodies.get(a).setSleepingAllowed(false);
            alternativeWorld.setAutoClearForces(false);
            alternativeWorld.setContinuousPhysics(true);
            alternativeWorld.setWarmStarting(true);
        }
    }

    public void simulateAlternativeWorld() {
        alternativeWorldTimer += Gdx.graphics.getDeltaTime();
        if (alternativeWorldTimer > 0.2f) {
            alternativeWorldTimer = 0;
            alternateAccumulator += 3f;
                while (alternateAccumulator > FPSCAP) {
                    alternativeWorld.step(FPSCAP, 10, 10);
                    alternateAccumulator -= FPSCAP;

                    float bodyAngle = alternativeBodies.get(0).getAngle();

                    float linearVelocityX = (float) Math.cos(bodyAngle + 1.45f) * shipSailingSpeed;
                    float linearVelocityY = (float) Math.sin(bodyAngle + 1.45f) * shipSailingSpeed;
                    Vector2 linearVelocity = alternativeBodies.get(0).getLinearVelocity();

                    if (bodyAngle < destinatedAngle) {
                        alternativeBodies.get(0).setAngularVelocity(0.55f);
                        alternativeBodies.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);
                    }

                    if (bodyAngle > destinatedAngle - 0.1f && bodyAngle < destinatedAngle + 0.1f) {
                        alternativeBodies.get(0).setAngularVelocity(0f);
                        //		this.get(0).setLinearVelocity(0,0);
                    }

                    if (bodyAngle > destinatedAngle) {
                        alternativeBodies.get(0).setAngularVelocity(-1 * 0.55f);
                        alternativeBodies.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);

                    }
                    if (bodyAngle < destinatedAngle - 0.1f && bodyAngle > destinatedAngle + 0.1f) {
                        alternativeBodies.get(0).setAngularVelocity(0f);
                        //		this.get(0).setLinearVelocity(0,0);
                    }

                    timerDot += FPSCAP;
                    if (timerDot > 1f) {
                        for (Body body : alternativeBodies) {
                            dots.add(new Dot(body.getPosition().x, body.getPosition().y, body.getAngle()));
                        }
                 timerDot = 0;
                    }
                }
                //
            }
        }


    public ArrayList<Body> getBodies() {
        return bodies;
    }

    public void setBodies(ArrayList<Body> bodies) {
        this.bodies = bodies;
    }

    public ArrayList<BodyDef> getBodyDefs() {
        return bodyDefs;
    }

    public void setBodyDefs(ArrayList<BodyDef> bodyDefs) {
        this.bodyDefs = bodyDefs;
    }

    public ArrayList<FixtureDef> getFixtureDefs() {
        return fixtureDefs;
    }

    public void setFixtureDefs(ArrayList<FixtureDef> fixtureDefs) {
        this.fixtureDefs = fixtureDefs;
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

    public void add(Body value, BodyDef bodyDef, FixtureDef fixtureDef) {
        value = world.createBody(bodyDef);
        value.createFixture(fixtureDef);

        bodies.add(value);
        bodyDefs.add(bodyDef);
        fixtureDefs.add(fixtureDef);

        alternativeBodies.add(alternativeWorld.createBody(bodyDef));
    }

    public void simulate(float deltaTime) {
        //How does it work?
        //http://saltares.com/blog/games/fixing-your-timestep-in-libgdx-and-box2d/
        //     alternativeWorldTimer += deltaTime;
        //   if (alternativeWorldTimer > 1) {
        //  for (int a = 0; a < 50; a++)
        //    simulateAlternativeWorld();
        //   alternativeWorldTimer = 0;
        // }
        accumulator += deltaTime;
        while (accumulator > FPSCAP) {
            world.step(FPSCAP, 6, 2);
            accumulator -= FPSCAP;

            float bodyAngle = bodies.get(0).getAngle();

            float linearVelocityX = (float) Math.cos(bodyAngle + 1.45f) * shipSailingSpeed;
            float linearVelocityY = (float) Math.sin(bodyAngle + 1.45f) * shipSailingSpeed;
            Vector2 linearVelocity = bodies.get(0).getLinearVelocity();


            if (bodyAngle < destinatedAngle) {
                bodies.get(0).setAngularVelocity(0.55f);
                bodies.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);
            }

            if (bodyAngle > destinatedAngle - 0.1f && bodyAngle < destinatedAngle + 0.1f) {
                bodies.get(0).setAngularVelocity(0f);
                //		this.get(0).setLinearVelocity(0,0);
            }

            if (bodyAngle > destinatedAngle) {
                bodies.get(0).setAngularVelocity(-1 * 0.55f);
                bodies.get(0).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);

            }
            if (bodyAngle < destinatedAngle - 0.1f && bodyAngle > destinatedAngle + 0.1f) {
                bodies.get(0).setAngularVelocity(0f);
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
        toTarget.x = clickedPoint.x - bodies.get(0).getPosition().x;
        toTarget.y = clickedPoint.y - bodies.get(0).getPosition().y;

        destinatedAngle = MathUtils.atan2(-toTarget.x, toTarget.y);

        destinatedX = mouseX;
        destinatedY = mouseY;


        pointsX = destinatedX - bodies.get(0).getPosition().x;
        pointsY = destinatedY - bodies.get(0).getPosition().y;

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