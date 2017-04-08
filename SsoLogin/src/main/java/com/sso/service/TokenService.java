package com.sso.service;

import com.yyl.common.collection.ResponseData;

public interface TokenService {

	ResponseData getUserByToken(String token);
}
