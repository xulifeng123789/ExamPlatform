package com.yyl.common.entity;

//公用常亮
public class Constant {

	public enum sex {
		MALE(0),
		FEMALE(1);
		
		private int val;

		private sex(int val) {
			this.val = val;
		}
		
	}
}
