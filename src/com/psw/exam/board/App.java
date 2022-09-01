package com.psw.exam.board;

import com.psw.exam.board.util.DBUtil;
import com.psw.exam.board.util.SecSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    if (rq.getUrlPath().equals("/usr/member/join")) {
      String loginId;
      String loginPw;
      String loginPwConfirm;
      String name;

      System.out.println("== 회원 가입 ==");

      // 로그인 아이디 입력
      while(true) {
        System.out.printf("로그인 아이디 : ");
        loginId = sc.nextLine().trim();

        if (loginId.length() == 0) {
          System.out.println("로그인 아이디를 입력해주세요.");
          continue;
        }

        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) > 0");
        sql.append("FROM member");
        sql.append("WHERE loginId = ?", loginId);

        boolean isLoginDup = DBUtil.selectRowBooleanValue(conn, sql);

        if(isLoginDup) {
          System.out.printf("%s(은)는 이미 사용중인 로그인 아이디입니다.\n", loginId);
          continue;
        }

        break;
      }

      // 로그인 비번 입력
      while (true) {
        System.out.printf("로그인 비번 : ");
        loginPw = sc.nextLine().trim();

        if(loginPw.length() == 0) {
          System.out.println("로그인 비번을 입력해주세요.");
        }

        boolean loginPwConfirmSame = true;

        while (true) {
          System.out.printf("로그인 비번확인 : ");
          loginPwConfirm = sc.nextLine().trim();

          if(loginPwConfirm.length() == 0) {
            System.out.println("로그인 비번확인을 입력해주세요.");
            continue;
          }

          if(loginPw.equals(loginPwConfirm) == false) {
            System.out.println("로그인 비번이 일치하지 않습니다. 다시 입력해주세요.");
            loginPwConfirmSame = false;
            break;
          }

          break;
        }

        if(loginPwConfirmSame) {
          break;
        }
      }

      // 이름 입력
      while (true) {
        System.out.printf("이름 : ");
        name = sc.nextLine().trim();

        if(name.length() == 0) {
          System.out.println("이름을 입력해주세요.");
          continue;
        }
        break;
      }

      SecSql sql = new SecSql();
      sql.append("INSERT INTO member");
      sql.append("SET regDate = NOW()");
      sql.append(", updateDate = NOW()");
      sql.append(", loginId = ?", loginId);
      sql.append(", loginPw = ?", loginPw);
      sql.append(", `name` = ?", name);

      DBUtil.insert(conn, sql);

      System.out.printf("%s님 환영합니다.\n", name);

    } else if (rq.getUrlPath().equals("/usr/article/write")) {
      System.out.println("== 게시물 등록 ==");
      System.out.printf("제목 : ");
      String title = sc.nextLine();
      System.out.printf("내용 : ");
      String body = sc.nextLine();

      SecSql sql = new SecSql();
      sql.append("INSERT INTO article");
      sql.append("SET regDate = NOW()");
      sql.append(", updateDate = NOW()");
      sql.append(", title = ?", title);
      sql.append(", `body` = ?", body);

      int id = DBUtil.insert(conn, sql);

      System.out.printf("%d번 게시물이 생성되었습니다.\n", id);

    } else if (rq.getUrlPath().equals("/usr/article/list")) {
      System.out.println("== 게시물 리스트 ==");

      List<Article> articles = new ArrayList<>();

      SecSql sql = new SecSql();
      sql.append("SELECT *");
      sql.append("FROM article");
      sql.append("ORDER BY id DESC");

      List <Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

      for(Map<String, Object> articleMap : articleListMap) {
        articles.add(new Article(articleMap));
      }

      if (articles.size() == 0) {
        System.out.println("게시물이 존재하지 않습니다.");
        return;
      }

      System.out.println("번호 / 제목");

      for (Article article : articles) {
        System.out.printf("%d / %s\n", article.id, article.title);
      }

    } else if (rq.getUrlPath().equals("/usr/article/detail")) {
      int id = rq.getIntParam("id", 0);

      if (id == 0) {
        System.out.println("id를 올바르게 입력해주세요.");
        return;
      }

      SecSql sql = new SecSql();
      sql.append("SELECT *");
      sql.append("FROM article");
      sql.append("WHERE id = ?", id);
      Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

      if(articleMap.isEmpty()) {
        System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
        return;
      }

      Article article = new Article(articleMap);

      System.out.printf("번호 : %d\n", article.id);
      System.out.printf("등록날짜 : %s\n", article.regDate);
      System.out.printf("수정날짜 : %s\n", article.updateDate);
      System.out.printf("제목 : %s\n", article.title);
      System.out.printf("내용 : %s\n", article.body);

    } else if (rq.getUrlPath().equals("/usr/article/delete")) {
      int id = rq.getIntParam("id", 0);

      if (id == 0) {
        System.out.println("id를 올바르게 입력해주세요.");
        return;
      }

      System.out.println("== 게시글 삭제 ==");

      SecSql sql = new SecSql();

      sql.append("SELECT COUNT(*) AS cnt");
      sql.append("FROM article");
      sql.append("WHERE id = ?", id);
      int articlesCount = DBUtil.selectRowIntValue(conn, sql);

      if (articlesCount == 0) {
        System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
        return;
      }

      sql = new SecSql();
      sql.append("DELETE FROM article");
      sql.append("WHERE id = ?", id);

      DBUtil.delete(conn, sql);

      System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);

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

      SecSql sql = new SecSql();
      sql.append("UPDATE article");
      sql.append("SET updateDate = NOW()");
      sql.append(", title = ?", title);
      sql.append(", `body` = ?", body);
      sql.append("WHERE id = ?", id);

      DBUtil.update(conn, sql);

      System.out.println("게시물이 수정되었습니다.");

    } else if (cmd.equals("exit")) {
      System.out.println("== 시스템 종료 ==");
      System.exit(0);
    }
  }

}

