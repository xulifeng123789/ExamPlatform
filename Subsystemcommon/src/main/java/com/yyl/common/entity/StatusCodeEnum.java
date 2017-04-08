package com.yyl.common.entity;

//状态代码枚举
public enum StatusCodeEnum {

	ARTICLE_NOT_FOUND("A003","查询的文章不存在"),
	GET_ARTICLE_EXCEPTION("A004","查询异常");
	
	public String code;
	public String message;
	
	private StatusCodeEnum(String code,String message) {
		this.code = code;
		this.message = message;
	}
}
