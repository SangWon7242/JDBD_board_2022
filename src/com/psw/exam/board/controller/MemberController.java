package com.psw.exam.board.controller;

import com.psw.exam.board.util.DBUtil;
import com.psw.exam.board.util.SecSql;

public class MemberController extends Controller {
  public void join() {
    String loginId;
    String loginPw;
    String loginPwConfirm;
    String name;

    System.out.println("== 회원 가입 ==");

    // 로그인 아이디 입력
    while (true) {
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

      if (isLoginDup) {
        System.out.printf("%s(은)는 이미 사용중인 로그인 아이디입니다.\n", loginId);
        continue;
      }

      break;
    }

    // 로그인 비번 입력
    while (true) {
      System.out.printf("로그인 비번 : ");
      loginPw = sc.nextLine().trim();

      if (loginPw.length() == 0) {
        System.out.println("로그인 비번을 입력해주세요.");
      }

      boolean loginPwConfirmSame = true;

      while (true) {
        System.out.printf("로그인 비번확인 : ");
        loginPwConfirm = sc.nextLine().trim();

        if (loginPwConfirm.length() == 0) {
          System.out.println("로그인 비번확인을 입력해주세요.");
          continue;
        }

        if (loginPw.equals(loginPwConfirm) == false) {
          System.out.println("로그인 비번이 일치하지 않습니다. 다시 입력해주세요.");
          loginPwConfirmSame = false;
          break;
        }

        break;
      }

      if (loginPwConfirmSame) {
        break;
      }
    }

    // 이름 입력
    while (true) {
      System.out.printf("이름 : ");
      name = sc.nextLine().trim();

      if (name.length() == 0) {
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
  }


}
