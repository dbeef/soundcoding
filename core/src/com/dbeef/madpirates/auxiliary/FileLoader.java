package com.dbeef.madpirates.auxiliary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by dbeef on 26.05.17.
 */
public class FileLoader {

    private Texture pirateShip;
    private Texture mapCross;
    private Texture map;
    private BitmapFont font;
    private ParticleEffect pe;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapOrthogonal;

    public Texture getPirateShip() {
        return pirateShip;
    }

    public Texture getMapCross() {
        return mapCross;
    }

    public Texture getMap() {
        return map;
    }

    public BitmapFont getFont() {
        return font;
    }

    public ParticleEffect getPe() {
        return pe;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public OrthogonalTiledMapRenderer getTiledMapOrthogonal() {
        return tiledMapOrthogonal;
    }

    public FileLoader(){
        pirateShip = new Texture("core/assets/pirateShips/pirateShip.png");
        pirateShip.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mapCross = new Texture("core/assets/others/compass.png");
        mapCross.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        map = new Texture("core/assets/others/map.png");
        map.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont();
        font.setColor(Color.RED);

        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("core/assets/particleEffects/piana"),
                Gdx.files.internal("core/assets/particleEffects"));


        tiledMap = new TmxMapLoader().load("core/assets/tileMap/worldTilemap.tmx");
        tiledMapOrthogonal = new OrthogonalTiledMapRenderer(tiledMap);

    }
}
