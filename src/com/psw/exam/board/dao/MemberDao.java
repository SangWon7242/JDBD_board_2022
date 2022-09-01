package com.psw.exam.board.dao;

import com.psw.exam.board.util.DBUtil;
import com.psw.exam.board.util.SecSql;

import java.sql.Connection;

public class MemberDao {
  private Connection conn;

  public MemberDao(Connection conn) {
    this.conn = conn;
  }

  public boolean isLoginedDup(String loginId) {
    SecSql sql = new SecSql();

    sql.append("SELECT COUNT(*) > 0");
    sql.append("FROM member");
    sql.append("WHERE loginId = ?", loginId);

    return DBUtil.selectRowBooleanValue(conn, sql);
  }

  public int join(String loginId, String loginPw, String name) {
    SecSql sql = new SecSql();
    sql.append("INSERT INTO member");
    sql.append("SET regDate = NOW()");
    sql.append(", updateDate = NOW()");
    sql.append(", loginId = ?", loginId);
    sql.append(", loginPw = ?", loginPw);
    sql.append(", `name` = ?", name);

    int id = DBUtil.insert(conn, sql);

    return id;
  }
}
