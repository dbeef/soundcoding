package com.dbeef.madpirates.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Ship {
    static float scale = 1.5f;
    static double sailSpeed = 20f;
    static double rotationSpeed = 20f;
    public PolygonShape shape;
    float destinatedX;
    float destinatedY;
    float destinatedAngle;
    float pointsX;
    float pointsY;
    ParticleEffect particleFoam;
    int simulationIndex;
    FixtureDef fixtureDef;
    BodyDef bodyDef;
    Body shipBody;
    Sprite shipSprite;
    double x;
    double y;
    double angle;

    public Ship(Texture sT, ParticleEffect pe, double xx, double yy) {

        particleFoam = pe;
//particleFoam.start();
        x = xx;
        y = yy;

        shipSprite = new Sprite(sT);
        shipSprite.setPosition((int) x, (int) y);
        shipSprite.setScale(scale);

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((float) x, (float) y);

        shape = new PolygonShape();
        shape.setAsBox((shipSprite.getWidth() / 2) * (float) scale, (shipSprite.getHeight() / 2) * (float) scale);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1;
    }

    public static float getScale() {
        return scale;
    }

    public static void setScale(float scale) {
        Ship.scale = scale;
    }

    public static double getSailSpeed() {
        return sailSpeed;
    }

    public static void setSailSpeed(double sailSpeed) {
        Ship.sailSpeed = sailSpeed;
    }

    public static double getRotationSpeed() {
        return rotationSpeed;
    }

    public static void setRotationSpeed(double rotationSpeed) {
        Ship.rotationSpeed = rotationSpeed;
    }

    public float getDestinatedX() {
        return destinatedX;
    }

    public void setDestinatedX(float destinatedX) {
        this.destinatedX = destinatedX;
    }

    public float getDestinatedY() {
        return destinatedY;
    }

    public void setDestinatedY(float destinatedY) {
        this.destinatedY = destinatedY;
    }

    public float getDestinatedAngle() {
        return destinatedAngle;
    }

    public void setDestinatedAngle(float destinatedAngle) {
        this.destinatedAngle = destinatedAngle;
    }

    public float getPointsX() {
        return pointsX;
    }

    public void setPointsX(float pointsX) {
        this.pointsX = pointsX;
    }

    public float getPointsY() {
        return pointsY;
    }

    public void setPointsY(float pointsY) {
        this.pointsY = pointsY;
    }

    public ParticleEffect getParticleFoam() {
        return particleFoam;
    }

    public void setParticleFoam(ParticleEffect particleFoam) {
        this.particleFoam = particleFoam;
    }

    public PolygonShape getShape() {
        return shape;
    }

    public void setShape(PolygonShape shape) {
        this.shape = shape;
    }

    public int getSimulationIndex() {
        return simulationIndex;
    }

    public void setSimulationIndex(int simulationIndex) {
        this.simulationIndex = simulationIndex;
    }

    public Body getShipBody() {
        return shipBody;
    }

    public void setShipBody(Body shipBody) {
        this.shipBody = shipBody;
    }

    public Sprite getShipSprite() {
        return shipSprite;
    }

    public void setShipSprite(Sprite shipSprite) {
        this.shipSprite = shipSprite;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Body getBody() {
        return shipBody;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public void render(Batch batch, float delta) {
//	particleFoam.draw(batch,delta);
        shipSprite.draw(batch);

    }

    public void getSimulationIndex(int simInd) {
        simulationIndex = simInd;
        shape.dispose();
    }

    public int giveSimulationIndex() {
        return simulationIndex;
    }

    public void takeSimulationReflection(Body body) {
        x = body.getPosition().x;
        y = body.getPosition().y;
        angle = MathUtils.radiansToDegrees * body.getAngle();

        shipSprite.setPosition((float) x - shipSprite.getWidth() / 2, (float) y - shipSprite.getHeight() / 2);
        shipSprite.setRotation((float) angle + 180);


        particleFoam.setPosition((float) x, (float) y);
    }

    public double giveX() {
        return (float) x - shipSprite.getWidth() / 2;
    }

    public double giveY() {
        return (float) y - shipSprite.getHeight() / 2;
    }

    public void setTextureReferences() {
    }
}
