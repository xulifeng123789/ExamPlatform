package com.yangyl.convert;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.yyl.common.collection.RequestBean;

public class RequestConvert implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(RequestBean.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		RequestBean result = new RequestBean(webRequest.getParameterMap().size());
		for(Map.Entry<String, String[]> entry : webRequest.getParameterMap().entrySet()) {
			String[] value = entry.getValue();
			String data;
			if(value == null)
				data = null;
			else if(value[0].isEmpty())
				data = null;
			else
				data = value[0];
			result.getParam().put(entry.getKey(), data);
		}
		try {
			result.setPage(Integer.parseInt(result.getParam().get("page")));
			result.setRows(Integer.parseInt(result.getParam().get("rows")));
		} catch (Exception e) {
		}
		
		return result;
	}

}
