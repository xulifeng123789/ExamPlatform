package com.sso.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.sso.dao.JedisClient;
import com.sso.service.LoginService;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.utils.CookieUtils;
import com.yyl.common.utils.json.JacksonUtil;
import com.yyl.common.utils.string.StringUtil;

public class LoginServiceImpl implements LoginService{

	
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_SESSION_KEY}")
	private String REDIS_SESSION_KEY;
	@Value("${REDIS_SESSION_EXPIRE}")
	private Integer REDIS_SESSION_EXPIRE;
	
	private static String TT_TOKEN="TT_TOKEN";
	@Override
	public ResponseData Login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		//有效性验证
		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
			return new ResponseData(false,"用户名或密码不能为空");
		}
		//数据库判断用户名密码是否正确
		Object user = new Object();
		//生成token
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		//把用户信息写入redis，清空密码，为了安全
		jedisClient.set(REDIS_SESSION_KEY+":"+token, JacksonUtil.bean2Json(user));
		jedisClient.expire(REDIS_SESSION_KEY+":"+token, REDIS_SESSION_EXPIRE);
		//把token写入cookie
		CookieUtils.setCookie(request, response, TT_TOKEN, token);
		return new ResponseData(true,"");
	}

}
