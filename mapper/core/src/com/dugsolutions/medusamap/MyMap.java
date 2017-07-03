package com.dugsolutions.medusamap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.Iterator;

/**
 * Created by dug on 6/27/17.
 */

public class MyMap {

    class MyRenderer extends OrthogonalTiledMapRenderer {
        public MyRenderer(TiledMap map) {
            super(map);
        }

        @Override
        public void renderObject(MapObject object) {
            if (object instanceof RectangleMapObject) {
                MapProperties props = object.getProperties();
                String value = props.get("text", String.class);
                if (value != null) {
                    Float fx = props.get("x", Float.class);
                    Float fy = props.get("y", Float.class);
                    Float fw = props.get("width", Float.class);
                    Float fh = props.get("height", Float.class);
                    int x = Math.round(fx);
                    int y = Math.round(fy);
                    int w = Math.round(fw);
                    int h = Math.round(fh);
                    font.draw(tiledMapRenderer.getBatch(), value, x + w / 4, y + h / 2);

                    if (!appliedInitial) {
                        value = props.get("initial", String.class);
                        Boolean initial = Boolean.parseBoolean(value);
                        if (initial != null && initial) {
                            initialX = x;
                            initialY = y;
                        }
                    }
                }
            }
        }
    }

    TiledMap tiledMap;
    MyRenderer tiledMapRenderer;
    BitmapFont font;
    boolean appliedInitial;
    Integer initialX;
    Integer initialY;

    public void create() {
        tiledMap = new TmxMapLoader().load("tiles/Map.tmx");
        tiledMapRenderer = new MyRenderer(tiledMap);
        font = new BitmapFont();
        Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
    }

    public void render(MyCamera camera) {
        if (!appliedInitial && initialX != null) {
            camera.move(initialX, initialY);
            appliedInitial = true;
        }
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void dispose() {
        tiledMap.dispose();
    }
}
