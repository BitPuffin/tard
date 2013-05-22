package com.thelonelybyte.tard;

public class CharacterAttributes {
	
	public int level;
	public long hp;
	public long maxhp;
	public long damage;
	
	public long experience;
	public long nextlevel;
	
	public long maxhpbase;
	public long damagebase;
	
	public CharacterAttributes(long basehp, long basedamage) {
		maxhpbase = basehp;
		damagebase = basedamage;
	}
	
}
