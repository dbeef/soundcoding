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
	
	ParticleEffect particleFoam;

	public PolygonShape shape;
	static float scale = 1.5f;
	int simulationIndex;
	FixtureDef fixtureDef;
	BodyDef bodyDef;
	Body shipBody;
	
	Sprite shipSprite;
	static double sailSpeed = 20f;
	static double rotationSpeed = 20f;
	double x;
	double y;
	double angle;
	
public Ship(Texture sT, ParticleEffect pe, double xx, double yy){

particleFoam = pe;
//particleFoam.start();
x = xx;
y = yy;

shipSprite = new Sprite(sT);
shipSprite.setPosition((int)x,(int)y);
shipSprite.setScale(scale);

bodyDef = new BodyDef();
bodyDef.type = BodyDef.BodyType.DynamicBody;
bodyDef.position.set((float)x,(float) y);

shape = new PolygonShape();
shape.setAsBox((shipSprite.getWidth()/2) *(float) scale, (shipSprite.getHeight()/2 )*(float) scale);
fixtureDef = new FixtureDef();
fixtureDef.shape = shape;
fixtureDef.density = 1f;
fixtureDef.friction = 1f;
fixtureDef.restitution = 1;
}

public Body getBody(){
return shipBody;	
}
public BodyDef getBodyDef(){
return bodyDef;	
}
public FixtureDef getFixtureDef(){
	return fixtureDef;
}

public void render(Batch batch, float delta){
//	particleFoam.draw(batch,delta);
shipSprite.draw(batch);

}
public void getSimulationIndex(int simInd){
	simulationIndex = simInd;
	shape.dispose();
}
public int giveSimulationIndex(){
	return simulationIndex;
}
public void takeSimulationReflection(Body body){
x = body.getPosition().x;	
y = body.getPosition().y;	
angle = MathUtils.radiansToDegrees * body.getAngle();

shipSprite.setPosition((float)x - shipSprite.getWidth()/2,(float) y - shipSprite.getHeight()/2);
shipSprite.setRotation((float)angle + 180);


particleFoam.setPosition((float)x, (float)y);
}
public double giveX(){return (float)x - shipSprite.getWidth()/2;}
public double giveY(){return (float) y - shipSprite.getHeight()/2;}
public void setTextureReferences(){}
}
