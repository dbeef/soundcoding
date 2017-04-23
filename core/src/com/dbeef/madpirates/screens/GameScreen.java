package com.dbeef.madpirates.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Curvature;
import com.bitfire.postprocessing.effects.Nfaa;
import com.dbeef.madpirates.Main;
import com.dbeef.madpirates.camera.ImprovedCamera;
import com.dbeef.madpirates.input.InputInterpreter;
import com.dbeef.madpirates.models.Dot;
import com.dbeef.madpirates.models.Ship;
import com.dbeef.madpirates.physics.BodiesDatabase;
import com.dbeef.madpirates.physics.MapBodyBuilder;
import com.dbeef.soundcoding.StarterBroadcaster;
import com.dbeef.soundcoding.StarterSniffer;
import com.dbeef.soundcoding.models.GameInformation;
import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.google.gson.Gson;

public class GameScreen implements Screen {


    private final Main game;
    private float simulationTime = 0;
    private float deltaTime;
    private ParticleEffect pe;
    private TiledMap tiledMap;
    boolean sniffing = false;
    private OrthogonalTiledMapRenderer tiledMapOrthogonal;
    private BitmapFont font;
    private Sprite mapCrossSprite;
    private Texture mapCross;
    private SpriteBatch batch;
    private Texture pirateShip;
    private Viewport viewport;
    private Box2DDebugRenderer dr;
    private Ship ship;
    private boolean waitingForInput;
    private boolean informationRetrieved;
    Thread broadcasterThread;
    private Ship enemyShip;
    private InputInterpreter iI;
    private BodiesDatabase bodiesDatabase;
    private ImprovedCamera camera;
    private OrthographicCamera guiCamera;
    private StarterBroadcaster broadcaster = new StarterBroadcaster();
    StarterSniffer sniffer = new StarterSniffer();
   private Viewport guiViewport;
    PostProcessor postProcessor;
    public GameScreen(final Main gam) {

        this.game = gam;

        font = new BitmapFont();
        font.setColor(Color.RED);

        System.out.println(Gdx.files.local("").list().length);
        for (int a = 0; a < Gdx.files.local("").list().length; a++) {
            System.out.println(Gdx.files.local("").list()[a]);
        }

        pirateShip = new Texture("core/assets/pirateShips/pirateShip.png");
        pirateShip.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        mapCross = new Texture("core/assets/others/compass.png");
        mapCross.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        mapCrossSprite = new Sprite(mapCross);
        batch = new SpriteBatch();
        camera = new ImprovedCamera(800, 480, 72 * 64,83 * 64, 28*64, 17*64);
       guiCamera = new OrthographicCamera(800,480);

        iI = new InputInterpreter();
        viewport = new FillViewport(800, 480, camera);
        guiViewport = new FillViewport(800, 480, guiCamera);

        bodiesDatabase = new BodiesDatabase();
        dr = new Box2DDebugRenderer();
        dr.setDrawVelocities(true);

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("core/assets/particleEffects/piana"),
                Gdx.files.internal("core/assets/particleEffects"));

        Vector2 centerOfMap = new Vector2();
        centerOfMap.x = 100 * 64 / 2;
        centerOfMap.y = 100 * 64 / 2;

        ship = new Ship(pirateShip, pe, centerOfMap.x, centerOfMap.y + 800);
        bodiesDatabase.add(ship.getBody(), ship.getBodyDef(), ship.getFixtureDef());

        enemyShip = new Ship(pirateShip, pe, 1700, 1700);
        bodiesDatabase.add(enemyShip.getBody(), enemyShip.getBodyDef(), enemyShip.getFixtureDef());

        camera.position.set(camera.viewportWidth / 2, (
                camera.viewportHeight / 2), 0);

        tiledMap = new TmxMapLoader().load("core/assets/tileMap/worldTilemap.tmx");
        tiledMapOrthogonal = new OrthogonalTiledMapRenderer(tiledMap);

        MapBodyBuilder b = new MapBodyBuilder();
        b.buildShapes(tiledMap, 64, bodiesDatabase, "Islands");
        b.buildShapes(tiledMap, 64, bodiesDatabase, "Map bounds");


        postProcessor = new PostProcessor( false, false, true );
        Curvature c = new com.bitfire.postprocessing.effects.Curvature();
        c.setDistortion(0.5f);
        postProcessor.addEffect(c);

        Thread snifferThread = new Thread(sniffer);
        snifferThread.setPriority(Thread.MAX_PRIORITY);
        snifferThread.start();
        broadcasterThread = new Thread(broadcaster);
    }


    @Override
    public void render(float delta) {

        deltaTime = Gdx.graphics.getDeltaTime();
        camera.takeZoomDelta(iI.getZoomDelta());

        if (iI.isTouched()) {
            bodiesDatabase.captureSimulation();
        System.out.println("capturing simulation");
        }

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
        if(simulationTime == 0)
         waitingForInput = true;
         //   simulationTime = 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
         if(!broadcasterThread.isAlive()) {
             broadcasterThread = new Thread(broadcaster);
             GameInformation information = new GameInformation();

             information.putVariable(GameInformationFrequencies.PLAYER_POS_X, (int) bodiesDatabase.getBodies().get(0).getPosition().x);
             information.putVariable(GameInformationFrequencies.PLAYER_POS_Y, (int) bodiesDatabase.getBodies().get(0).getPosition().y);

             Gson gson = new Gson();
             String json = gson.toJson(information);
             GameInformationFrequencies gameInformationFrequencies = new GameInformationFrequencies();
            json =  gameInformationFrequencies.translateJSONToSound(json);
             broadcaster.setMessage(json);
             broadcasterThread.start();
         }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.takeZoomDelta(deltaTime);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
        sniffing = !sniffing;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            camera.takeZoomDelta(-deltaTime);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            bodiesDatabase.setShipSailingSpeed(bodiesDatabase.getShipSailingSpeed() + deltaTime * 0.25f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            bodiesDatabase.setShipSailingSpeed(bodiesDatabase.getShipSailingSpeed() - deltaTime * 0.25f);
        }
        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);


        postProcessor.capture();
        tiledMapOrthogonal.setView(camera);
        tiledMapOrthogonal.render();
        batch.begin();

        //Better flush the buffer before drawing box2d's debug mode
        batch.flush();
        batch.end();
        batch.begin();


        float a = 0;
        for (Dot dot : bodiesDatabase.getDots()) {
            mapCrossSprite.setRotation((float) Math.toDegrees(dot.getAngle()));
            mapCrossSprite.setPosition(dot.getX() - 30, dot.getY() - 30);

            if (a != 1) {
                mapCrossSprite.setColor(1, 1, 1, 1 - a);
                mapCrossSprite.draw(batch);
            }
            a += 0.0025f;
            if (a > 1)
                a = 1;
        }
    //     dr.render(bodiesDatabase.getWorld(), camera.combined);

        ship.render(batch, delta);
        enemyShip.render(batch, delta);

        batch.setProjectionMatrix(guiCamera.combined);

        if(waitingForInput != true) {
            if (simulationTime == 0)
                font.draw(batch, "Tura planowania. P by zakończyc ture.", 130, 220);
            else
                font.draw(batch, "Tura symulacji.", 130, 220);
        }
        else
        {
            font.draw(batch, "Oczekiwanie na informacje od drugiego gracza", 30, 220);
            font.draw(batch, "Wcisnij G aby rozeslac informacje", 30, 180);
            font.draw(batch, "Wcisnij L aby nasluchiwac informacje", 30, 140);
            font.draw(batch, "Nasluchiwanie informacji: " + sniffing, 30, 100);
        }

        batch.end();

        postProcessor.render();
        if (simulationTime > 0) {
            simulationTime -= deltaTime;
            if (simulationTime < 0)
                simulationTime = 0;
        }

        if (simulationTime > 0)
            bodiesDatabase.simulate(delta * 7);

        bodiesDatabase.simulateAlternativeWorld();
        ship.takeSimulationReflection(bodiesDatabase.getBodies().get(0));
        enemyShip.takeSimulationReflection(bodiesDatabase.getBodies().get(1));

        camera.setPosition((float) ship.giveX() + 30, (float)ship.giveY() + 70);

        if (iI.touched == true) {
            Vector3 temp = new Vector3();
            temp.x = (float) iI.getXTap();
            temp.y = (float) iI.getYTap();
            camera.unproject(temp);
            bodiesDatabase.sideToRotate(temp.x, temp.y);
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }
}