package com.psw.exam.board;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
  public void run() {
    Scanner sc = Container.scanner;

    while (true) {
      System.out.printf("명령어) ");
      String cmd = sc.nextLine().trim();
      Rq rq = new Rq(cmd);

      // DB 연결시작
      Connection conn = null;
      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        System.err.println("예외 : MySQL 드라이버 클래스가 없습니다.");
        System.out.println("프로그램을 종료합니다.");
        break;
      }
      String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
      try {
        conn = DriverManager.getConnection(url, "sbsst", "sbs123414");

        doAction(rq, conn, sc, cmd);

      } catch (SQLException e) {
        System.err.println("예외 : DB에 연결할 수 없습니다..");
        System.out.println("프로그램을 종료합니다.");
        break;
      } finally {
        try {
          if (conn != null && !conn.isClosed()) {
            conn.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      // DB 연결 끝
    }
    sc.close();
  }

  private void doAction(Rq rq, Connection conn, Scanner sc, String cmd) {
    if (rq.getUrlPath().equals("/usr/article/write")) {
      System.out.println("== 게시물 등록 ==");
      System.out.printf("제목 : ");
      String title = sc.nextLine();
      System.out.printf("내용 : ");
      String body = sc.nextLine();


      PreparedStatement pstat = null;

      try {

        String sql = "INSERT INTO article";
        sql += " SET regDate = NOW()";
        sql += ", updateDate = NOW()";
        sql += ", title = \"" + title + "\"";
        sql += ", `body` = \"" + body + "\";";

        pstat = conn.prepareStatement(sql);
        pstat.executeUpdate();

      } catch (SQLException e) {
        System.out.println("에러: " + e);
      } finally {
        try {
          if (pstat != null && !pstat.isClosed()) {
            pstat.close();
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    } else if (rq.getUrlPath().equals("/usr/article/list")) {
      System.out.println("== 게시물 리스트 ==");

      PreparedStatement pstat = null;
      ResultSet rs = null;

      List<Article> articles = new ArrayList<>();

      try {

        String sql = "SELECT *";
        sql += " FROM article";
        sql += " ORDER BY id DESC";

        pstat = conn.prepareStatement(sql);
        rs = pstat.executeQuery(sql);

        while (rs.next()) {
          int id = rs.getInt("id");
          String regDate = rs.getString("regDate");
          String updateDate = rs.getString("updateDate");
          String title = rs.getString("title");
          String body = rs.getString("body");

          Article article = new Article(id, regDate, updateDate, title, body);
          articles.add(article);
        }

      } catch (SQLException e) {
        System.out.println("에러: " + e);
      }

      if (articles.size() == 0) {
        System.out.println("게시물이 존재하지 않습니다.");
        return;
      }

      System.out.println("번호 / 제목");

      for (Article article : articles) {
        System.out.printf("%d / %s\n", article.id, article.title);
      }

    } else if (rq.getUrlPath().equals("/usr/article/modify")) {
      int id = rq.getIntParam("id", 0);

      if (id == 0) {
        System.out.println("id를 올바르게 입력해주세요.");
        return;
      }

      System.out.printf("새 제목 : ");
      String title = sc.nextLine();
      System.out.printf("새 내용 : ");
      String body = sc.nextLine();

      PreparedStatement pstat = null;

      try {

        String sql = "UPDATE article";
        sql += " SET updateDate = NOW()";
        sql += ", title = \"" + title + "\"";
        sql += ", `body` = \"" + body + "\"";
        sql += " WHERE id = " + id;

        pstat = conn.prepareStatement(sql);
        pstat.executeUpdate();

      } catch (SQLException e) {
        System.out.println("에러: " + e);
      }
    } else if (cmd.equals("exit")) {
      System.out.println("== 시스템 종료 ==");
      System.exit(0);
    }
  }

}

