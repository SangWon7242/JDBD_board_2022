package com.psw.exam.board;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
  public void run() {
    Scanner sc = Container.scanner;
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

        Connection conn = null;
        PreparedStatement pstat = null;

        try {
          Class.forName("com.mysql.jdbc.Driver");

          String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

          conn = DriverManager.getConnection(url, "sbsst", "sbs123414");

          String sql = "INSERT INTO article";
          sql += " SET regDate = NOW()";
          sql += ", updateDate = NOW()";
          sql += ", title = \"" + title + "\"";
          sql += ", `body` = \"" + body + "\";";

          pstat = conn.prepareStatement(sql);
          pstat.executeUpdate();

        } catch (ClassNotFoundException e) {
          System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
          System.out.println("에러: " + e);
        } finally {
          try {
            if (conn != null && !conn.isClosed()) {
              conn.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          try {
            if (pstat != null && !pstat.isClosed()) {
              pstat.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
        ++articleLastId;
      }
     else if(cmd.equals("/usr/article/list")) {
        System.out.println("== 게시물 리스트 ==");

        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;

        List<Article> articles = new ArrayList<>();

        try {
          Class.forName("com.mysql.jdbc.Driver");

          String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

          conn = DriverManager.getConnection(url, "sbsst", "sbs123414");

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

        } catch (ClassNotFoundException e) {
          System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
          System.out.println("에러: " + e);
        } finally {
          try {
            if (rs != null && !rs.isClosed()) {
              rs.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          try {
            if (pstat != null && !pstat.isClosed()) {
              pstat.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          try {
            if (conn != null && !conn.isClosed()) {
              conn.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }

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
