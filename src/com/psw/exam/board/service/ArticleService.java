package com.psw.exam.board.service;

import com.psw.exam.board.Container;
import com.psw.exam.board.dto.Article;
import com.psw.exam.board.dao.ArticleDao;

import java.util.List;

public class ArticleService {
  private ArticleDao articleDao;

  public ArticleService() {
    articleDao = Container.articleDao;
  }

  public int add(int memberId, String title, String body) {
    return articleDao.add(memberId, title, body);
  }

  public boolean articleExists(int id) {
    return articleDao.articleExists(id);
  }

  public void delete(int id) {
    articleDao.delete(id);
  }

  public Article getArticleById(int id) {
    return articleDao.getArticleById(id);
  }

  public void update(int id, String title, String body) {
    articleDao.update(id, title, body);
  }

  public List<Article> getArticles() {
    return articleDao.getArticles();
  }
}
