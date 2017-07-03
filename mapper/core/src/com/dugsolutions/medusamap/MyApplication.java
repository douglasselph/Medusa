package com.dugsolutions.medusamap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Timer;

public class MyApplication extends ApplicationAdapter {

	MyMap map = new MyMap();
	MyCamera camera = new MyCamera();

	@Override
	public void create () {
		camera.create();
		map.create();
		Gdx.input.setInputProcessor(new InputPan(camera));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		map.render(camera);
	}
	
	@Override
	public void dispose () {
		map.dispose();
	}
}
