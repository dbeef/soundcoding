package com.dbeef.madpirates.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dbeef.madpirates.models.Dot;
import com.dbeef.madpirates.models.Ship;

import java.util.ArrayList;

public class BodiesDatabase {

    public static final float FPSCAP = 1 / 60F;

    public Ship getPlayerShip() {
        return playerShip;
    }

    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }

    public Ship getEnemyShip() {
        return enemyShip;
    }

    public void setEnemyShip(Ship enemyShip) {
        this.enemyShip = enemyShip;
    }

    Ship playerShip;
    Ship enemyShip;
    float shipSailingSpeed;
    float shipManouverSpeed;
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
        this.playerShip = playerShip;
        this.enemyShip = enemyShip;
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

    public void drawTrajectory(Batch batch, Sprite cross) {
        float a = 0;
        for (Dot dot : this.getDots()) {
            cross.setRotation((float) Math.toDegrees(dot.getAngle()));
            cross.setPosition(dot.getX() - 30, dot.getY() - 30);

            if (a != 1) {
                cross.setColor(1, 1, 1, 1 - a);
                cross.draw(batch);
            }
            a += 0.0025f;
            if (a > 1)
                a = 1;
        }
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

                simulateShip(playerShip, 0, alternativeBodies);
                simulateShip(enemyShip, 1, alternativeBodies);

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

            simulateShip(playerShip, 0, bodies);
            simulateShip(enemyShip, 1,bodies);

        }
    }

    public float playerSideToRotate(float mouseX, float mouseY) {

        //Trigonometry, we've got mouse position and ship position
        //We want an angle between X axis and mouse click location

        Vector2 clickedPoint = new Vector2();
        clickedPoint.set((int) mouseX, (int) mouseY);
        Vector2 toTarget = new Vector2();
        toTarget.x = clickedPoint.x - bodies.get(0).getPosition().x;
        toTarget.y = clickedPoint.y - bodies.get(0).getPosition().y;

        playerShip.setDestinatedAngle(MathUtils.atan2(-toTarget.x, toTarget.y));
        playerShip.setDestinatedX(mouseX);
        playerShip.setDestinatedY(mouseY);

        playerShip.setPointsX(playerShip.getDestinatedX() - bodies.get(0).getPosition().x);
        playerShip.setPointsY(playerShip.getDestinatedY() - bodies.get(0).getPosition().y);

//Now we've got destinatedAngle in radians

        return ((float) playerShip.getDestinatedAngle());
    }

    public float enemySideToRotate(float mouseX, float mouseY) {

        //Trigonometry, we've got mouse position and ship position
        //We want an angle between X axis and mouse click location

        Vector2 clickedPoint = new Vector2();
        clickedPoint.set((int) mouseX, (int) mouseY);
        Vector2 toTarget = new Vector2();
        toTarget.x = clickedPoint.x - bodies.get(1).getPosition().x;
        toTarget.y = clickedPoint.y - bodies.get(1).getPosition().y;

        enemyShip.setDestinatedAngle(MathUtils.atan2(-toTarget.x, toTarget.y));
        enemyShip.setDestinatedX(mouseX);
        enemyShip.setDestinatedY(mouseY);

        enemyShip.setPointsX(enemyShip.getDestinatedX() - bodies.get(1).getPosition().x);
        enemyShip.setPointsY(enemyShip.getDestinatedY() - bodies.get(1).getPosition().y);

//Now we've got destinatedAngle in radians

        return ((float) enemyShip.getDestinatedAngle());
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

    private void simulateShip(Ship ship, int index, ArrayList<Body> b) {

        float bodyAngle = b.get(index).getAngle();

        float linearVelocityX = (float) Math.cos(bodyAngle + 1.45f) * shipSailingSpeed;
        float linearVelocityY = (float) Math.sin(bodyAngle + 1.45f) * shipSailingSpeed;

        Vector2 linearVelocity = b.get(index).getLinearVelocity();

        if (bodyAngle < ship.getDestinatedAngle()) {
            b.get(index).setAngularVelocity(0.55f);
            b.get(index).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);
        }

        if (bodyAngle > ship.getDestinatedAngle() - 0.1f && bodyAngle < ship.getDestinatedAngle() + 0.1f) {
            b.get(index).setAngularVelocity(0f);
            //		this.get(0).setLinearVelocity(0,0);
        }

        if (bodyAngle > ship.getDestinatedAngle()) {
            b.get(index).setAngularVelocity(-1 * 0.55f);
            b.get(index).setLinearVelocity(linearVelocity.x + linearVelocityX, linearVelocity.y + linearVelocityY);

        }
        if (bodyAngle < ship.getDestinatedAngle() - 0.1f && bodyAngle > ship.getDestinatedAngle() + 0.1f) {
            b.get(index).setAngularVelocity(0f);
            //		this.get(0).setLinearVelocity(0,0);
        }
    }
}