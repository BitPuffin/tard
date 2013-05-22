package com.thelonelybyte.tard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

// @Author

public class BlockType {
	private String type;
	private Sprite sprite;
	private Texture tex;
	
	public BlockType(String type, Texture texture,  Sprite sprite) {
		this.type = type;
		this.sprite = sprite;
		tex = texture;
	}
	
	public String getType() {
		return type;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public Texture getTexture() {
		return tex;
	}
}
