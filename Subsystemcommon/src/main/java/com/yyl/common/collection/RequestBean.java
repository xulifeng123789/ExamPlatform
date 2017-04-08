package com.yyl.common.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private int page;
	private int rows;
	
	private Map<String,String> param;
	
	public RequestBean(int size) {
		this.param = new HashMap<String, String>(size);
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

}
