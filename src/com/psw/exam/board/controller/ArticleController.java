package com.psw.exam.board.controller;

import com.psw.exam.board.Container;
import com.psw.exam.board.dto.Article;
import com.psw.exam.board.Rq;
import com.psw.exam.board.service.ArticleService;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class ArticleController extends Controller {

  private ArticleService articleService;

  public ArticleController() {
    articleService = Container.articleService;
  }

  public void add(String cmd) {
    if ( Container.session.isLogined() == false ) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

    int memberId = Container.session.loginedMemberId;

    System.out.println("== 게시물 등록 ==");
    System.out.printf("제목 : ");
    String title = sc.nextLine();
    System.out.printf("내용 : ");
    String body = sc.nextLine();

    int id = articleService.add(memberId, title, body);

    System.out.printf("%d번 게시물이 생성되었습니다.\n", id);
  }

  public void showList(String cmd) {
    System.out.println("== 게시물 리스트 ==");

    List<Article> articles = articleService.getArticles();


    if (articles.size() == 0) {
      System.out.println("게시물이 존재하지 않습니다.");
      return;
    }

    System.out.println("번호 / 작성날짜 / 작성자 / 제목");

    for (Article article : articles) {
      System.out.printf("%d / %s / %s / %s\n", article.id, article.regDate, article.extra__writer, article.title);
    }
  }

  public void showDetail(Rq rq, String cmd) {
    int id = rq.getIntParam("id", 0);

    if (id == 0) {
      System.out.println("id를 올바르게 입력해주세요.");
      return;
    }

    articleService.increaseHit(id);
    Article article = articleService.getArticleById(id);

    if (article == null) {
      System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
      return;
    }

    System.out.printf("번호 : %d\n", article.id);
    System.out.printf("등록날짜 : %s\n", article.regDate);
    System.out.printf("수정날짜 : %s\n", article.updateDate);
    System.out.printf("작성자 : %s\n", article.extra__writer);
    System.out.printf("조회수 : %s\n", article.hit);
    System.out.printf("제목 : %s\n", article.title);
    System.out.printf("내용 : %s\n", article.body);
  }

  public void delete(Rq rq, String cmd) {
    if ( Container.session.isLogined() == false ) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

    int id = rq.getIntParam("id", 0);

    if (id == 0) {
      System.out.println("id를 올바르게 입력해주세요.");
      return;
    }

    System.out.printf("== %d번 게시글 삭제 ==\n", id);

    Article article = articleService.getArticleById(id);

    if (article == null) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    if ( article.memberId != Container.session.loginedMemberId ) {
      System.out.printf("권한이 없습니다.\n");
      return;
    }

    articleService.delete(id);

    System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);
  }

  public void modify(Rq rq, String cmd) {
    if ( Container.session.isLogined() == false ) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }

    int id = rq.getIntParam("id", 0);

    if (id == 0) {
      System.out.println("id를 올바르게 입력해주세요.");
      return;
    }

    Article article = articleService.getArticleById(id);

    if ( article == null ) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    }

    if ( article.memberId != Container.session.loginedMemberId ) {
      System.out.printf("권한이 없습니다.\n");
      return;
    }

    System.out.printf("새 제목 : ");
    String title = sc.nextLine();
    System.out.printf("새 내용 : ");
    String body = sc.nextLine();

   articleService.update(id, title, body);

    System.out.println("게시물이 수정되었습니다.");

  }
}
