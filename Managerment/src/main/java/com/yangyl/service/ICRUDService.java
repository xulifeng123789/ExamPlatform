package com.yangyl.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;

public interface ICRUDService<DOMAIN> {

	public ResponseData select(RequestBean bean);
	
	public ResponseData selectOne(int id);
	
	public ResponseData update(DOMAIN domain);
	
	public ResponseData insert(DOMAIN domain);
	
	public ResponseData delete(int id);
	
	public ByteArrayOutputStream export(RequestBean domain);
	
	
	public ResponseData batchInsert(String mapperName,List<DOMAIN> datas);
	
	public ResponseData batchUpdate(String mapperName,List<DOMAIN> datas);
	
	public ResponseData batchDelete(String mapperName,List<DOMAIN> datas,Function<DOMAIN,Integer> idSupplier);
}
