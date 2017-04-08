package com.yyl.common.utils.excel;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.yyl.common.utils.DateUtils;

public class ExcelRow extends ArrayList<String>{

	private static final long serialVersionUID = 1L;
	
	public boolean add(String data) {
		return super.add(data == null ? "" : data);
	}
	public boolean add(int data) {
		return super.add(Integer.toString(data));
	}
	public boolean add(float data) {
		return super.add(Float.toString(data));
	}
	public boolean add(double data) {
		return super.add(Double.toString(data));
	}
	public boolean add(boolean data) {
		return super.add(data ? "是" : "否");
	}
	
	public boolean add(Timestamp data) {
		return super.add(DateUtils.TimestampToDateString(data));
	}
}
