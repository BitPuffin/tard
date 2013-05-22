package com.thelonelybyte.tard;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tard extends Game implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	//private Sprite sprite;
	
	public static BlockType ground;
	public static BlockType wall;
	public static BlockType stairsup;
	public static BlockType stairsdown;
	
	public static DungeonManager dm;
	
	Texture groundTexture;
	Texture wallTexture;
	Texture stairsUpTexture;
	Texture stairsDownTexture;
	
	DungeonGenerator generator;
	
	private int currentLevel = 0;
	

	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		dm = new DungeonManager();
		generator = new DungeonGenerator();
		
		Gdx.app.log("init", "Loading textures!");
		groundTexture = new Texture(Gdx.files.internal("data/ground.png"));
		wallTexture = new Texture(Gdx.files.internal("data/wall.png"));
		stairsUpTexture = new Texture(Gdx.files.internal("data/ugly_up_stairs.png"));
		stairsDownTexture = new Texture(Gdx.files.internal("data/stairs.png"));
		groundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		wallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		stairsUpTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		stairsDownTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Sprite groundSprite = new Sprite(groundTexture);
		Sprite wallSprite = new Sprite(wallTexture);
		Sprite stairsDownSprite = new Sprite(stairsDownTexture);
		Sprite stairsUpSprite = new Sprite(stairsUpTexture);
		
		ground = new BlockType("ground", groundSprite);
		wall = new BlockType("wall", wallSprite);
		stairsup = new BlockType("stairsup", stairsUpSprite);
		stairsdown = new BlockType("stairsdown", stairsDownSprite);
		
		Gdx.app.log("init", "Generating dungeon!");
		Dungeon d = generator.genDungeon(wall, ground);
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		CharacterAttributes playerattr = new CharacterAttributes(100, 5);
		//Character player = new Character();
		
		//texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		//sprite = new Sprite(region);
		//sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		groundTexture.dispose();
		wallTexture.dispose();
		stairsUpTexture.dispose();
		stairsDownTexture.dispose();
		batch.dispose();
		texture.dispose();
		
	}

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0.12f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//sprite.draw(batch);
		batch.end();
	}
	
	public void update(float delta) {
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
