package com.thelonelybyte.tard;

// @Author: Isak Andersson

public class Character {
	CharacterAttributes attributes;
	private int[] position;
	
	public Character(CharacterAttributes attr, int[] pos) {
		attributes = attr;
		reset(pos);
	}
	
	public void reset(int[] pos) {
		position = pos;
		attributes.level = 1;
		attributes.maxhp = attributes.maxhpbase;
		attributes.hp = attributes.maxhp;
		attributes.damage = attributes.damagebase;
		attributes.experience = 0;
		attributes.nextlevel = 50;
	}
	
	public boolean isDead() {
		return attributes.hp <= 0;
	}
	
	public void levelUp() {
		attributes.level++;
		attributes.maxhp *= 1.20;
		attributes.hp = attributes.maxhp;
		attributes.damage += 5;
		attributes.experience = 0;
		attributes.nextlevel *= 1.5;
	}
	
	public void harm(int amount) {
		attributes.hp -= amount;
	}
	
	public int getLevel() {
		return attributes.level;
	}
	
	public int[] getPos() {
		return position;
	}
	
	public void setPos(int[] pos) {
		position = pos;
	}
	
	public void moveUp() {
		position[1]++;
	}
	public void moveDown() {
		position[1]--;
	}
	public void moveRight() {
		position[0]++;
	}
	public void moveLeft() {
		position[0]--;
	}
}
