package com.yangyl.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.yangyl.domain.Article;
import com.yangyl.wrap.ArticleWrap;
import com.yangyl.wrap.ICRUDWrap;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;
import com.yyl.common.entity.CacheDefine;
import com.yyl.common.utils.excel.ExcelTools;

@Service
public class ArticleService extends AbstractCRUDService<Article>{

	@Resource
	private ArticleWrap articleWrap;
	
	@Override
	public ICRUDWrap<Article> getWrap() {
		// TODO Auto-generated method stub
		return articleWrap;
	}
	
	@Override
	public ResponseData insert(Article domain) {
		// TODO Auto-generated method stub
		//设置更新插入日期，提交人等等
		return super.insert(domain);
	}
	
	@Override
	@Cacheable("user")
	public ResponseData selectOne(int id) {
		// TODO Auto-generated method stub
		return super.selectOne(id);
	}
	
	@Override
	public ByteArrayOutputStream export(RequestBean domain) {
		// 根据不同标志，调不同的导出
		return null;
		
	}

	@Override
	protected String getMessage() {
		// TODO Auto-generated method stub
		return CacheDefine.ADD_ARTICLE;
	}


}
