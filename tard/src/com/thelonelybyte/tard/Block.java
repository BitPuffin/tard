package com.thelonelybyte.tard;

public class Block {
		public static final int size = 25;
		private BlockType type;
		
		public Block(BlockType type) {
			this.type = type;
		}
		
		public BlockType getType() {
			return type;
		}
}
