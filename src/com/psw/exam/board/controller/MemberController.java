package com.psw.exam.board.controller;

import com.psw.exam.board.Rq;
import com.psw.exam.board.service.MemberService;
import com.psw.exam.board.util.DBUtil;
import com.psw.exam.board.util.SecSql;

import java.sql.Connection;
import java.util.Scanner;

public class MemberController extends Controller {

  private MemberService memberService;

  public MemberController(Connection conn, Scanner sc, Rq rq) {
    super(sc, rq);
    memberService = new MemberService(conn);
  }

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

      boolean isLoginedDup = memberService.isLoginedDup(loginId);

      if (isLoginedDup) {
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

    int id = memberService.join(loginId, loginPw, name);

    System.out.printf("%s님 환영합니다.\n", name);
  }


}
