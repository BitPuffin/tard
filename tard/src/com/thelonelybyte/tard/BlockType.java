package com.thelonelybyte.tard;

import com.badlogic.gdx.graphics.g2d.Sprite;

// @Author: Isak Andersson

public class BlockType {
	private String type;
	private Sprite sprite;
	
	public BlockType(String type, Sprite sprite) {
		this.type = type;
		this.sprite = sprite;
	}
	
	public String getType() {
		return type;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
}
