package com.psw.exam.board.service;

import com.psw.exam.board.Container;
import com.psw.exam.board.dao.MemberDao;
import com.psw.exam.board.dto.Member;

import java.sql.Connection;

public class MemberService {
  private MemberDao memberDao;

  public MemberService() {
    memberDao = Container.memberDao;
  }
  public boolean isLoginedDup(String loginId) {
   return memberDao.isLoginedDup(loginId);
  }

  public int join(String loginId, String loginPw, String name) {
    return memberDao.join(loginId, loginPw, name);
  }

  public Member getMemberByLoginId(String loginId) {
    return memberDao.getMemberByLoginId(loginId);
  }
}
