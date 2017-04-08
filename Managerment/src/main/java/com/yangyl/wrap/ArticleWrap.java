package com.yangyl.wrap;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yangyl.dao.ArticleMapper;
import com.yangyl.dao.ICRUDMapper;
import com.yangyl.domain.Article;
import com.yangyl.domain.PageBean;
import com.yyl.common.collection.ResponseData;
import com.yyl.common.collection.RequestBean;
import com.yyl.common.entity.CacheDefine;
/**
 * 再继承一次，是为了方便添加缓存
 * @author yangyl
 *
 */
@Repository
public class ArticleWrap extends AbstractCRUDWrap<Article> {

	@Resource
	private ArticleMapper articleMapper;

	@Override
	public ICRUDMapper<Article> getMapper() {
		// TODO Auto-generated method stub
		return articleMapper;
	}
	
//	@Cacheable(CacheDefine.INFO_SKU)
	public List<Article> listArticlesByCatalog(RequestBean domain) {
		return articleMapper.listArticlesByCatalog(domain);
	}
	
}
