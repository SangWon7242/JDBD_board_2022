package com.psw.exam.board.session;

import com.psw.exam.board.dto.Member;

public class Session {
  public int loginedMemberId;
  public Member loginedMember;

  public Session() {
    loginedMemberId = -1;
  }
}
