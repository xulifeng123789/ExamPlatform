package com.test.redis;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yangyl.domain.Article;
import com.yangyl.service.ArticleService;
import com.yangyl.service.ICRUDService;
import com.yyl.common.collection.ResponseData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml"})
public class TestExampleService {

	@Resource
	private ArticleService articleService;
	
	@Test
	public void deleteTest() {
		articleService.delete(4);
	}
	
	@Test
	public void addTest() {
		Article article = new Article();
		article.setArticle_content("1234");
		article.setArticle_date(new Date());
		article.setArticle_title("抗战胜利");
		article.setCategory_Name("历史");
		ResponseData data = articleService.insert(article);
		System.out.println(((Article)data.getData()).getArticle_Id());
	}
	
	@Test
	public void selectTest() {
		Article article = (Article)articleService.selectOne(5).getData();
		System.out.println(article.getArticle_Id());
	}
}
