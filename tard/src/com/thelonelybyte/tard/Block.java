package com.thelonelybyte.tard;

public class Block {
		public static final int size = 64;
		private BlockType type;
		
		public Block(BlockType type) {
			this.type = type;
		}
		
		public BlockType getType() {
			return type;
		}
}
