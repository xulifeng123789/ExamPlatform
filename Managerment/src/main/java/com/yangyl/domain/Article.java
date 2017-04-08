package com.yangyl.domain;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
	
    

	/**
	 * 
	 */
	private static final long serialVersionUID = 1399959012961259789L;

	private int article_Id;

    private String article_title;

    private Date article_date;

    private String article_content;

    private String category_Name;

	public int getArticle_Id() {
		return article_Id;
	}

	public void setArticle_Id(int article_Id) {
		this.article_Id = article_Id;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	
	public String getCategory_Name() {
		return category_Name;
	}

	public void setCategory_Name(String category_Name) {
		this.category_Name = category_Name;
	}

	public Date getArticle_date() {
		return article_date;
	}

	public void setArticle_date(Date article_date) {
		this.article_date = article_date;
	}

	public String getArticle_content() {
		return article_content;
	}

	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}
	
   
}