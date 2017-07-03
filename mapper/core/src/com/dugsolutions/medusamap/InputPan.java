package com.dugsolutions.medusamap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by dug on 6/27/17.
 */

public class InputPan implements InputProcessor {

    MyCamera camera;

    public InputPan(MyCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            camera.panStart(-1, 0);
        } else if (keycode == Input.Keys.RIGHT) {
            camera.panStart(1, 0);
        } else if (keycode == Input.Keys.UP) {
            camera.panStart(0, 1);
        } else if (keycode == Input.Keys.DOWN) {
            camera.panStart(0, -1);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        camera.panStop();
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
}
