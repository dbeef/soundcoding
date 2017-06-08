package com.dbeef.madpirates.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Curvature;
import com.dbeef.madpirates.Main;
import com.dbeef.madpirates.auxiliary.FileLoader;
import com.dbeef.madpirates.camera.ImprovedCamera;
import com.dbeef.madpirates.hud.HUD;
import com.dbeef.madpirates.input.InputInterpreter;
import com.dbeef.madpirates.input.KeyboardInterpreter;
import com.dbeef.madpirates.models.Ship;
import com.dbeef.madpirates.physics.BodiesDatabase;
import com.dbeef.madpirates.physics.MapBodyBuilder;
import com.dbeef.soundcoding.StarterSniffer;
import com.dbeef.soundcoding.models.GameInformationFrequencies;
import com.dbeef.soundcoding.utils.SoundifiedJsonDecoder;

public class GameScreen implements Screen {

    private final Main game;
    private SoundifiedJsonDecoder soundifiedJsonDecoder;
    private Thread soundifiedJsonDecoderThread;
    private StarterSniffer sniffer = new StarterSniffer();
    private PostProcessor postProcessor;
    private float deltaTime;
    private Sprite cross;
    private SpriteBatch batch;

    private Viewport viewport;
    private Box2DDebugRenderer dr;
    private Ship playerShip;
    private Ship enemyShip;
    private InputInterpreter iI;
    private BodiesDatabase bodiesDatabase;
    private ImprovedCamera camera;
    private OrthographicCamera guiCamera;
    private Viewport guiViewport;
    private FileLoader fileLoader;
    private KeyboardInterpreter keyboardInterpreter;

    public GameScreen(final Main gam) {


        this.game = gam;

        /*
        Niepotrzebne, przyda się do debugowania odczytu pliku z wiadomością, ale nic więcej
        System.out.println(Gdx.files.local("").list().length);
        for (int a = 0; a < Gdx.files.local("").list().length; a++) {
            System.out.println(Gdx.files.local("").list()[a]);
        }
        */
        //tu przypisać obiekty z file loadera


        iI = new InputInterpreter();
        fileLoader = new FileLoader();


        soundifiedJsonDecoder = new SoundifiedJsonDecoder("");
        soundifiedJsonDecoderThread = new Thread(soundifiedJsonDecoder);
        keyboardInterpreter = new KeyboardInterpreter(sniffer, soundifiedJsonDecoder, iI);

        cross = new Sprite(fileLoader.getMapCross());
        batch = new SpriteBatch();

        camera = new ImprovedCamera(800, 480, 72 * 64, 83 * 64, 28 * 64, 17 * 64);
        guiCamera = new OrthographicCamera(800, 480);

        viewport = new FillViewport(800, 480, camera);
        guiViewport = new FillViewport(800, 480, guiCamera);


        Vector2 centerOfMap = new Vector2();
        centerOfMap.x = 100 * 64 / 2;
        centerOfMap.y = 100 * 64 / 2;

        bodiesDatabase = new BodiesDatabase();

        Vector2 playerPosition = new Vector2();
        playerPosition.x = centerOfMap.x;
        playerPosition.y = centerOfMap.y + 800;

        Vector2 enemyPosition = new Vector2();
        enemyPosition.x = 1700;
        enemyPosition.y = 1700;
        //https://www.reddit.com/r/libgdx/comments/323ysb/how_to_pass_parameter_for_the_launcher/

        System.out.println(System.getProperty("player"));

        if(System.getProperty("player").equals("1")){
            playerShip = new Ship(fileLoader.getPirateShip(), fileLoader.getPe(),playerPosition.x, playerPosition.y);
            enemyShip = new Ship(fileLoader.getPirateShip(), fileLoader.getPe(), enemyPosition.x, enemyPosition.y);
        }
        if(System.getProperty("player").equals("2")){
            playerShip = new Ship(fileLoader.getPirateShip(), fileLoader.getPe(),enemyPosition.x, enemyPosition.y);
            enemyShip = new Ship(fileLoader.getPirateShip(), fileLoader.getPe(), playerPosition.x, playerPosition.y);
        }


        bodiesDatabase.setPlayerShip(playerShip);
        bodiesDatabase.setEnemyShip(enemyShip);


        bodiesDatabase.add(playerShip.getBody(), playerShip.getBodyDef(), playerShip.getFixtureDef());

        bodiesDatabase.add(enemyShip.getBody(), enemyShip.getBodyDef(), enemyShip.getFixtureDef());

        bodiesDatabase.setShipSailingSpeed(2);

        camera.position.set(camera.viewportWidth / 2, (
                camera.viewportHeight / 2), 0);

        MapBodyBuilder b = new MapBodyBuilder();
        b.buildShapes(fileLoader.getTiledMap(), 64, bodiesDatabase, "Islands");
        b.buildShapes(fileLoader.getTiledMap(), 64, bodiesDatabase, "Map bounds");

        postProcessor = new PostProcessor(false, false, true);
        Curvature c = new com.bitfire.postprocessing.effects.Curvature();
        c.setDistortion(0.5f);
        postProcessor.addEffect(c);

        //TODO
        //To poniżej do wywalenia do jakiejś nowej klasy łączącej sniffer + decoder +
        //obsługującej flagi
        Thread snifferThread = new Thread(sniffer);
        snifferThread.start();

    }


    @Override
    public void render(float delta) {

        deltaTime = Gdx.graphics.getDeltaTime();
        camera.takeZoomDelta(iI.getZoomDelta());

        keyboardInterpreter.update(bodiesDatabase, camera, iI);

        //TODO
        //To samo co @up

        updateSniffer();

        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        postProcessor.capture();
        fileLoader.getTiledMapOrthogonal().setView(camera);
        fileLoader.getTiledMapOrthogonal().render();
        batch.begin();

        //Better flush the buffer before drawing box2d's debug mode
        batch.flush();
        batch.end();

        postProcessor.render();
        batch.begin();

        bodiesDatabase.drawTrajectory(batch, cross);

        drawShips(delta);

        batch.setProjectionMatrix(guiCamera.combined);
        HUD.draw(batch, keyboardInterpreter.getInputModel(), fileLoader.getFont(), keyboardInterpreter.getStarterBroadcaster(), keyboardInterpreter.getBroadcasterThread(), soundifiedJsonDecoder, sniffer);

        batch.end();

        float simulationTime = keyboardInterpreter.getInputModel().getSimulationTime();
        if (simulationTime > 0) {
            simulationTime -= deltaTime;
            if (simulationTime < 0)
                simulationTime = 0;
        }
        keyboardInterpreter.getInputModel().setSimulationTime(simulationTime);

        if (simulationTime > 0)
            bodiesDatabase.simulate(delta * 7);

        bodiesDatabase.simulateAlternativeWorld();
        playerShip.takeSimulationReflection(bodiesDatabase.getBodies().get(0));
        enemyShip.takeSimulationReflection(bodiesDatabase.getBodies().get(1));

        System.out.println(soundifiedJsonDecoder.isJsonParsedSuccessfully());

        camera.setPosition((float) playerShip.giveX() + 30, (float) playerShip.giveY() + 70);

        if (iI.touched == true) {
            Vector3 temp = new Vector3();
            temp.x = (int) iI.getXTap();
            temp.y = (int) iI.getYTap();
            camera.unproject(temp);
            bodiesDatabase.playerSideToRotate(temp.x, temp.y);
        }
        if(soundifiedJsonDecoder.isJsonParsedSuccessfully()){
            if(soundifiedJsonDecoder.getGameInformation() != null && (soundifiedJsonDecoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_X)) != null && (soundifiedJsonDecoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_Y) != null)){

            Vector3 temp = new Vector3();
            temp.x = (soundifiedJsonDecoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_X));
            temp.y =  (soundifiedJsonDecoder.getGameInformation().getVariablesAndValues().get(GameInformationFrequencies.PLAYER_POS_Y));
            camera.unproject(temp);
            bodiesDatabase.enemySideToRotate(temp.x, temp.y);

                bodiesDatabase.captureSimulation();
                System.out.println("Capturing simulation.");

        soundifiedJsonDecoder.setGameInformation(null);
        soundifiedJsonDecoder.setMessage(null);
        soundifiedJsonDecoder.setJsonParsedSuccessfully(false);
        sniffer.clearDetectedMessages();
            }
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

    private void debugPhysics() {
        if (dr == null) {
            dr = new Box2DDebugRenderer();
            dr.setDrawVelocities(true);
        }
        dr.render(bodiesDatabase.getWorld(), camera.combined);
    }

    private void drawShips(float delta) {
        playerShip.render(batch, delta);
        enemyShip.render(batch, delta);
    }
    private void updateSniffer(){
        if ((soundifiedJsonDecoderThread == null || !soundifiedJsonDecoderThread.isAlive()) && soundifiedJsonDecoder.getGameInformation() == null) {
            String message = sniffer.getMessage();
            soundifiedJsonDecoder = new SoundifiedJsonDecoder(message);
            keyboardInterpreter.setDecoder(soundifiedJsonDecoder);
            soundifiedJsonDecoderThread = new Thread(soundifiedJsonDecoder);
            soundifiedJsonDecoderThread.start();
        }
        if (soundifiedJsonDecoder.getGameInformation() != null) {
            System.out.println(soundifiedJsonDecoder.getGameInformation().toString());
        }
        sniffer.setSniffing(keyboardInterpreter.getInputModel().isSniffing());
    }
}