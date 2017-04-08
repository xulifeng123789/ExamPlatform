package com.yangyl.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.yangyl.mq.producer.ProducerService;
import com.yangyl.service.ICRUDService;
import com.yangyl.wrap.ICRUDWrap;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;
import com.yyl.common.datasource.DynamicDataSourceHolder;
import com.yyl.common.entity.CacheDefine;

public abstract class AbstractCRUDService<DOMAIN> implements ICRUDService<DOMAIN>,InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(ICRUDService.class);
	
	private ICRUDWrap<DOMAIN> wrap;
	
	@Resource
	private ProducerService producerService;
	
	protected abstract String getMessage();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		setWrap(getWrap());
	}
	
	protected void sendMQ() {
		producerService.sendMessage(getMessage());
	}
	
	@Override
	public ResponseData select(RequestBean bean) {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.markSlave();
		if(bean.getPage() > 0)
			return wrap.select(bean);
		else
			return wrap.selectAll(bean);
	}
	
	@Override
	public ResponseData selectOne(int id) {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.markSlave();
		try {
			DOMAIN result = wrap.selectOne(id);
			return new ResponseData(result);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}
	
	@Override
	public ResponseData update(DOMAIN domain) {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.markMaster();
		try {
			DOMAIN result = wrap.update(domain);
			sendMQ();
			return new ResponseData(result);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}

	@Override
	public ResponseData insert(DOMAIN domain) {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.markMaster();
		try {
			DOMAIN result = wrap.insert(domain);
			sendMQ();
			return new ResponseData(result);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}

	@Override
	public ResponseData delete(int id) {
		// TODO Auto-generated method stub
		DynamicDataSourceHolder.markMaster();
		try {
			wrap.delete(id);
			sendMQ();
			return new ResponseData(true,"");
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}

	public abstract ICRUDWrap<DOMAIN> getWrap();
	public void setWrap(ICRUDWrap<DOMAIN> wrap) {
		this.wrap = wrap;
	}

	/**
	 * 批量
	 */
	@Override
	public ResponseData batchInsert(String mapperName,List<DOMAIN> datas) {
		DynamicDataSourceHolder.markMaster();
		try {
			wrap.batchInsert(mapperName,datas);
			sendMQ();
			return new ResponseData(true,"");
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}
	
	@Override
	public ResponseData batchUpdate(String mapperName,List<DOMAIN> datas) {
		DynamicDataSourceHolder.markMaster();
		try {
			wrap.batchUpdate(mapperName,datas);
			sendMQ();
			return new ResponseData(true,"");
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}
	
	@Override
	public ResponseData batchDelete(String mapperName,List<DOMAIN> datas,Function<DOMAIN,Integer> idSupplier) {
		DynamicDataSourceHolder.markMaster();
		try {
			wrap.batchDelete(mapperName,datas,idSupplier);
			sendMQ();
			return new ResponseData(true,"");
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			return new ResponseData(false, e.getMessage());
		}
	}

}
