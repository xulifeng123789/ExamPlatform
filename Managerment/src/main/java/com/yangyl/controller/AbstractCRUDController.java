package com.yangyl.controller;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangyl.service.ICRUDService;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;
import com.yyl.common.utils.DateUtils;

public abstract class AbstractCRUDController<DOMAIN> implements ICRUDController<DOMAIN>,InitializingBean {

	private ICRUDService<DOMAIN> service;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		setService(getService());
	}
	
	@Override
	@RequestMapping(value="/table",method=RequestMethod.GET)
	@ResponseBody
	public ResponseData select(RequestBean bean) {
		// TODO Auto-generated method stub
		return service.select(bean);
	}
	
	@Override
	@RequestMapping(value="/table/{id}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseData selectOne(@PathVariable int id) {
		// TODO Auto-generated method stub
		return service.selectOne(id);
	}
	
	@Override
	@RequestMapping(value="/table",method=RequestMethod.PUT)
	@ResponseBody
	public ResponseData update(DOMAIN domain) {
		// TODO Auto-generated method stub
		return service.update(domain);
	}

	@Override
	@RequestMapping(value="/table",method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insert(DOMAIN domain) {
		// TODO Auto-generated method stub
		return service.insert(domain);
	}

	@Override
	@RequestMapping(value="/table",method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseData delete(int id) {
		// TODO Auto-generated method stub
		return service.delete(id);
	}
	
	@Override
	@RequestMapping(value="/excel",method=RequestMethod.GET)
	public ResponseEntity<byte[]> export(RequestBean bean) {
		ByteArrayOutputStream bOutputStream = service.export(bean);
		
		String fileName = null;
		try {
			fileName = new String((bean.getParam().get("filename")+DateUtils.formatDate(new Date(), "yyyy-MM-dd")+".xls").getBytes(), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        return new ResponseEntity<byte[]>(bOutputStream.toByteArray(),headers,HttpStatus.CREATED);
	}

	public abstract ICRUDService<DOMAIN> getService();
	
	public void setService(ICRUDService<DOMAIN> service) {
		this.service = service;
	}
	
	

}
