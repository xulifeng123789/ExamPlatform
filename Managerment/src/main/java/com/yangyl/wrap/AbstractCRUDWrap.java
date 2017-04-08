package com.yangyl.wrap;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yangyl.dao.ICRUDMapper;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;

public abstract class AbstractCRUDWrap<DOMAIN> implements ICRUDWrap<DOMAIN>,InitializingBean {

	private ICRUDMapper<DOMAIN> mapper;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		setMapper(getMapper());
	}
	
	@Transactional(readOnly=true)
	public ResponseData select(RequestBean bean) {
		// TODO Auto-generated method stub
		PageHelper.startPage(bean.getPage(), bean.getRows());
		List<DOMAIN> list = mapper.select(bean.getParam());
		PageInfo<DOMAIN> pageInfo = new PageInfo<DOMAIN>(list);
		ResponseData result = new ResponseData(list);
		result.setPage(pageInfo.getPageNum());
		result.setPageSize(pageInfo.getPages());
		result.setTotal((int)pageInfo.getTotal());
		return result;
	}

	@Transactional(readOnly=true)
	public ResponseData selectAll(RequestBean bean) {
		// TODO Auto-generated method stub
		List<DOMAIN> list = mapper.select(bean.getParam());
		return new ResponseData(list);
	}
	
	@Transactional(readOnly=true)
	public DOMAIN selectOne(int id) {
		// TODO Auto-generated method stub
		return mapper.selectOne(id);
		
	}

	@Transactional(readOnly=false)
	public DOMAIN insert(DOMAIN domain) {
		// TODO Auto-generated method stub
		mapper.insert(domain);
		return domain;
	}

	@Transactional(readOnly=false)
	public DOMAIN update(DOMAIN domain) {
		// TODO Auto-generated method stub
	    mapper.update(domain);
	    return domain;
	}

	@Transactional(readOnly=false)
	public void delete(int id) {
		// TODO Auto-generated method stub
		mapper.delete(id);
	}

	public abstract ICRUDMapper<DOMAIN> getMapper();
	public void setMapper(ICRUDMapper<DOMAIN> mapper) {
		this.mapper = mapper;
	}

	/**
	 * 批量操作
	 */
	@Resource
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	@Transactional(readOnly = false)
	public void batchInsert(String mapperName,List<DOMAIN> datas) {
		SqlSession session = sqlSessionFactory.openSession();

		try {
			datas.forEach(data -> session.insert(mapperName + ".insert",data));
			session.commit();
		} catch (RuntimeException e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void batchUpdate(String mapperName,List<DOMAIN> datas) {
		SqlSession session = sqlSessionFactory.openSession();

		try {
			datas.forEach(data -> session.insert(mapperName + ".update",data));
			session.commit();
		} catch (RuntimeException e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void batchDelete(String mapperName,List<DOMAIN> datas,Function<DOMAIN,Integer> idSupplier) {
		SqlSession session = sqlSessionFactory.openSession();

		try {
			datas.forEach(data -> session.insert(mapperName + ".delete",
					idSupplier.apply(data)));
			session.commit();
		} catch (RuntimeException e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}
	}
}
