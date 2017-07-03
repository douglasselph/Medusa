package com.dugsolutions.medusamap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by dug on 6/27/17.
 */

public class MyCamera extends OrthographicCamera {

    enum State {
        Stable,
        Moving,
        Panning,
    }

    static final int STEP_SZ = 15;

    State state = State.Stable;
    int dx;
    int dy;

    public MyCamera() {
        super();
    }

    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        setToOrtho(false, w, h);
        translate(w/2, h/2);
        update();
    }

    public void panStart(int dx, int dy) {
        this.dx = dx * STEP_SZ;
        this.dy = dy * STEP_SZ;
        state = State.Panning;
    }

    public void panStop() {
        state = State.Stable;
    }

    public void move(int x, int y) {
        dx = x;
        dy = y;
        state = State.Moving;
    }

    @Override
    public void update() {
        if (state == State.Panning) {
            translate(dx, dy);
        } else if (state == State.Moving) {
            position.set(dx, dy, position.z);
            state = State.Stable;
        }
        super.update();
    }
}
