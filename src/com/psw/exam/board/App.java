package com.psw.exam.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
  public void run() {
    Scanner sc = Container.scanner;
    List<Article> articles = new ArrayList<>();
    int articleLastId = 0;

    while (true) {
      System.out.printf("명령어) ");
      String cmd = sc.nextLine().trim();
      if(cmd.equals("/usr/article/write")) {
        System.out.println("== 게시물 등록 ==");
        System.out.printf("제목 : ");
        String title = sc.nextLine();
        System.out.printf("내용 : ");
        String body = sc.nextLine();

        int id = ++articleLastId;

        Article article = new Article(id, title, body);

        articles.add(article);

        System.out.println("생성된 게시물 객체 : " + article);
        System.out.printf("%d번 게시물이 등록 되었습니다.\n", article.id);

      }
     else if(cmd.equals("/usr/article/list")) {
        System.out.println("== 게시물 리스트 ==");

        if( articles.size() == 0 ) {
          System.out.println("게시물이 존재하지 않습니다.");
          continue;
        }

        System.out.println("번호 / 제목");

        for( Article article : articles ) {
          System.out.printf("%d / %s\n", article.id, article.title);
        }

      }
      else if(cmd.equals("exit")) {
        System.out.println("== 시스템 종료 ==");
        break;
      }
    }
    sc.close();
  }
}
