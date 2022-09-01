package com.psw.exam.board.controller;

import com.psw.exam.board.Rq;

import java.sql.Connection;
import java.util.Scanner;

public abstract class Controller {
  protected Connection conn;
  protected Scanner sc;

  protected Rq rq;

  public void setConn(Connection conn) {
    this.conn = conn;
  }

  public void setScanner(Scanner sc) {
    this.sc = sc;
  }

  public void setRq(Rq rq) {
    this.rq = rq;
  }
}
