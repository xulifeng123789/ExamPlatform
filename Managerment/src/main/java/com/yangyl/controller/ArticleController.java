package com.yangyl.controller;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangyl.domain.Article;
import com.yangyl.service.ArticleService;
import com.yangyl.service.ICRUDService;
import com.yyl.common.collection.RequestBean;
import com.yyl.common.utils.DateUtils;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractCRUDController<Article> {

	@Resource
	private ArticleService articleService;

	@Override
	public ICRUDService<Article> getService() {
		// TODO Auto-generated method stub
		return articleService;
	}
	
}
