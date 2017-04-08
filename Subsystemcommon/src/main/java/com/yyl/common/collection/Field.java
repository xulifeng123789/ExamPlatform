package com.yyl.common.collection;

public class Field {
	private String field;
	private String tooltips;
	
	public Field() {
	}
	
	public Field(String field, String tooltips) {
		this.field = field;
		this.tooltips = tooltips;
	}

	public String getTooltips() {
		return tooltips;
	}
	public void setTooltips(String tooltips) {
		this.tooltips = tooltips;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}
