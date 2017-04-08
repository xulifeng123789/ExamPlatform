package com.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yyl.common.collection.ResponseData;

public interface LoginService {

	ResponseData Login(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
