package com.yangyl.controller;

import org.springframework.http.ResponseEntity;

import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;

public interface ICRUDController<DOMAIN> {

	public ResponseData select(RequestBean bean);
	
	public ResponseData selectOne(int id);
	
	public ResponseData update(DOMAIN domain);
	
	public ResponseData insert(DOMAIN domain);
	
	public ResponseData delete(int id);
	
	public ResponseEntity<byte[]> export(RequestBean bean);
}
