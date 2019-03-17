package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Vector;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Texture img1;
	private BitmapFont myBitmapFont;
	private Music musicBackbround;
	private int intX;
	private com.badlogic.gdx.math.Rectangle pigRectangle,coinsRectangle;
	private OrthographicCamera orthographicCamery;
	private Vector3 objVertor3;
	private Array<com.badlogic.gdx.math.Rectangle> objCoinsDrop;
	private long lastDropTime;
	private Iterator<com.badlogic.gdx.math.Rectangle> objIterator;
	private Sound soundSuccess , soundfalse;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("rabbit-128x128.png");
		img1 = new Texture("Carrot-64.png");

		orthographicCamery = new OrthographicCamera();
		orthographicCamery.setToOrtho(false,800,480);
		batch = new SpriteBatch();

		pigRectangle = new com.badlogic.gdx.math.Rectangle();
		pigRectangle.x = 368;
		pigRectangle.y = 20;
		pigRectangle.width = 64;
		pigRectangle.height = 64;

		myBitmapFont = new BitmapFont();
		myBitmapFont.setColor(Color.BLUE);

		objCoinsDrop = new Array<com.badlogic.gdx.math.Rectangle>();
		gameCoinsDrop();

		musicBackbround = Gdx.audio.newMusic(Gdx.files.internal("Hidde & Xjcode - Sirens WAV.wav"));
		soundSuccess = Gdx.audio.newSound(Gdx.files.internal("smb_coin.wav"));
		soundfalse = Gdx.audio.newSound(Gdx.files.internal("smb_gameover.wav"));

		musicBackbround.setLooping(true);
		musicBackbround.play();

	}
	private void gameCoinsDrop() {

		coinsRectangle = new com.badlogic.gdx.math.Rectangle();
		coinsRectangle.x = MathUtils.random(0,736);
		coinsRectangle.y = 480;
		coinsRectangle.width = 64;
		coinsRectangle.height = 64;
		objCoinsDrop.add(coinsRectangle);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(166/255.0f, 200/255.0f, 240/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		orthographicCamery.update();

		batch.setProjectionMatrix(orthographicCamery.combined);

		batch.begin();
		batch.draw(img, pigRectangle.x, pigRectangle.y);

		for(com.badlogic.gdx.math.Rectangle forRectangle : objCoinsDrop){
			batch.draw(img1,forRectangle.x,forRectangle.y);
		}
		batch.end();

		if (Gdx.input.isTouched()){
			objVertor3 = new Vector3();
			objVertor3.set(Gdx.input.getX(),Gdx.input.getY(),0);
			orthographicCamery.unproject(objVertor3);
			pigRectangle.x = objVertor3.x - 32;

		}
		if (pigRectangle.x < 0){
			pigRectangle.x = 0;
		}
		if (pigRectangle.x > 736) {
			pigRectangle.x = 736;
		}
		if(TimeUtils.nanoTime() - lastDropTime > 1E9){
			gameCoinsDrop();
		}
		objIterator = objCoinsDrop.iterator();
		while (objIterator.hasNext()){
			Rectangle objMyCoins = objIterator.next();
			objMyCoins.y -= 200 * Gdx.graphics.getDeltaTime();
			if(objMyCoins.y + 64 < 0 ){
				objIterator.remove();
				soundfalse.play();
			}
			if(objMyCoins.overlaps(pigRectangle)){
				soundSuccess.play();
				objIterator.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
