package com.yangyl.dao;

import java.util.List;

import com.yangyl.domain.Article;
import com.yyl.common.collection.RequestBean;

public interface ArticleMapper extends ICRUDMapper<Article> {
	
	List<Article> listArticlesByCatalog(RequestBean domain);
}