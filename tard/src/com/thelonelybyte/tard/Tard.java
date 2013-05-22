package com.thelonelybyte.tard;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

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
	
	// Test dungeon
	Dungeon d;
	
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
		groundSprite.setOrigin(groundSprite.getWidth()/2, groundSprite.getHeight()/2);
		wallSprite.setOrigin(groundSprite.getWidth()/2, groundSprite.getHeight()/2);
		//.setOrigin(groundSprite.getWidth()/2, groundSprite.getHeight()/2);
		//groundSprite.setOrigin(groundSprite.getWidth()/2, groundSprite.getHeight()/2);
		
		ground = new BlockType("ground", groundTexture, groundSprite);
		wall = new BlockType("wall", wallTexture, wallSprite);
		stairsup = new BlockType("stairsup", stairsUpTexture, stairsUpSprite);
		stairsdown = new BlockType("stairsdown", stairsDownTexture, stairsDownSprite);
		
		Gdx.app.log("init", "Generating dungeon!");
		d = generator.genDungeon(wall, ground);
		
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
		camera.zoom -= 2000*camera.zoom;
		camera.update();
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
	
	Room todraw = null;
	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0.15f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int x = 0; x < todraw.getWidth(); x++) {
			for (int y = 0; y < todraw.getHeight(); y++) {
				Sprite s = todraw.getBlockAt(x, y).getType().getSprite();
				
				//s.setSize(0.1f, 0.1f);
				s.setPosition(x*Block.size, y*Block.size);
				s.draw(batch);
			}
		}
		batch.end();
	}
	
	int relativex = 0;
	int relativey = 0;
	public void update(float delta) {
		if (todraw == null) {
			todraw = d.getRandomRoom();
		}
		// select room to draw with directional keys
		// If the room doesn't have an entrance to the room you pressed it won't switch room
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			int index = todraw.getNextRoomIndex(DungeonGenerator.up); 
			if ( index != -1) {
				todraw = d.getRoom(index);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			int index = todraw.getNextRoomIndex(DungeonGenerator.right); 
			if ( index != -1) {
				todraw = d.getRoom(index);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			int index = todraw.getNextRoomIndex(DungeonGenerator.down); 
			if ( index != -1) {
				todraw = d.getRoom(index);
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			int index = todraw.getNextRoomIndex(DungeonGenerator.left); 
			if ( index != -1) {
				todraw = d.getRoom(index);
			}
		}
		
		// Move the camera around with mouse and holding down left control
		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
			camera.translate(5*Gdx.input.getDeltaX(), 5*-Gdx.input.getDeltaY());
			camera.update();
		}
		Gdx.app.log("Camera", "Position x: " + camera.position.x + " Position y: " + camera.position.y);
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
