package com.psw.exam.board;

import com.psw.exam.board.controller.ArticleController;
import com.psw.exam.board.controller.MemberController;
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

        action(rq, conn, sc, cmd);

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

  private void action(Rq rq, Connection conn, Scanner sc, String cmd) {
    ArticleController articleController = new ArticleController(conn, sc, rq);
    MemberController memberController = new MemberController(conn, sc, rq);

    if (rq.getUrlPath().equals("/usr/member/join")) {
      memberController.join();
    } else if (rq.getUrlPath().equals("/usr/member/login")) {
      memberController.login();
    } else if (rq.getUrlPath().equals("/usr/article/write")) {
      articleController.add(cmd);
    } else if (rq.getUrlPath().equals("/usr/article/list")) {
      articleController.showList(cmd);
    } else if (rq.getUrlPath().equals("/usr/article/detail")) {
      articleController.showDetail(rq, cmd);
    } else if (rq.getUrlPath().equals("/usr/article/delete")) {
      articleController.delete(rq, cmd);
    } else if (rq.getUrlPath().equals("/usr/article/modify")) {
      articleController.modify(rq, cmd);
    } else if (cmd.equals("exit")) {
      System.out.println("== 시스템 종료 ==");
      System.exit(0);
    }
  }

}

