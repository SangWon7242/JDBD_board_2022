package com.psw.exam.board.dto;

import java.util.Map;

public class Member {
  public int id;
  public String regDate;
  public String updateDate;
  public String loginId;
  public String loginPw;
  public String name;

  public Member(Map<String, Object> memberMap) {
    this.id = (int) memberMap.get("id");
    this.loginId = (String) memberMap.get("loginId");
    this.loginPw = (String) memberMap.get("loginPw");
    this.regDate = (String) memberMap.get("regDate");
    this.updateDate = (String) memberMap.get("updateDate");
    this.name = (String) memberMap.get("name");
  }

  @Override
  public String toString() {
    return "Member{" +
        "id=" + id +
        ", regDate='" + regDate + '\'' +
        ", updateDate='" + updateDate + '\'' +
        ", loginId='" + loginId + '\'' +
        ", loginPw='" + loginPw + '\'' +
        ", name='" + name + '\'' +
        '}';
  }

}
