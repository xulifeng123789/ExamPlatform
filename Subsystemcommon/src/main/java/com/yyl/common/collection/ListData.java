package com.yyl.common.collection;

import java.util.List;

public class ListData {
	//分页数据
	private List<?> rows;
	//当前页
	private int page = 1;
	//每页的大小
	private int pageSize = 10;
	//总数
	private int total;
	//请求是否成功
	private boolean success;
	
	private String errorMsg;
	
	public ListData() {
	}
	public ListData(List<?> rows) {
		this.rows = rows;
		this.total = rows.size();
	}
	
	public ListData(boolean success,String errorMsg) {
		this.success = success;
		this.errorMsg = errorMsg;
	}
	
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
