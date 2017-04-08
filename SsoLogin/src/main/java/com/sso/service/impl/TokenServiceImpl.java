package com.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.sso.dao.JedisClient;
import com.sso.service.TokenService;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.utils.json.JacksonUtil;
import com.yyl.common.utils.string.StringUtil;

public class TokenServiceImpl implements TokenService{

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;
	@Value("${REDIS_SESSION_EXPIRE}")
	private Integer REDIS_SESSION_EXPIRE;
	
	@Override
	public ResponseData getUserByToken(String token) {
		// TODO Auto-generated method stub
		String json = jedisClient.get(REDIS_SESSION_KEY+":"+token);
		//没有取到用户信息
		if (StringUtil.isEmpty(json)) {
			return new ResponseData(false,"用户已过期");
		}
		//把json数据转换成对象
		Object user = JacksonUtil.json2Bean(json, Object.class);
		//更新session过期时间
		jedisClient.expire(REDIS_SESSION_KEY+":"+token, REDIS_SESSION_EXPIRE);
		return new ResponseData(user);
	}

}
