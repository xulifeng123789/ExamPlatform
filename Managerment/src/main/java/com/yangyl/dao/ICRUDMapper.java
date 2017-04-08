package com.yangyl.dao;

import java.util.List;
import java.util.Map;

public interface ICRUDMapper<DOMAIN> {

	public List<DOMAIN> select(Map<String,String> param);
	
	public DOMAIN selectOne(int id);
	
	public void insert(DOMAIN domain);
	
	public void update(DOMAIN domain);
	
	public void delete(int id);
}
