package com.yangyl.wrap;

import java.util.List;
import java.util.function.Function;

import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;

public interface ICRUDWrap<DOMAIN> {

	public ResponseData select(RequestBean bean);
	
	public DOMAIN selectOne(int id);
	
	public ResponseData selectAll(RequestBean bean);
	
	public DOMAIN insert(DOMAIN domain);
	
	public DOMAIN update(DOMAIN domain);
	
	public void delete(int id);
	
	
	public void batchInsert(String mapperName,List<DOMAIN> datas);
	
	public void batchUpdate(String mapperName,List<DOMAIN> datas);
	
	public void batchDelete(String mapperName,List<DOMAIN> datas,Function<DOMAIN,Integer> idSupplier);
}
