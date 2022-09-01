package com.psw.exam.board.controller;

import com.psw.exam.board.Rq;

import java.util.Scanner;

public abstract class Controller {
  protected Rq rq;
  protected Scanner sc;


  public Controller(Scanner sc, Rq rq) {
    this.sc = sc;
    this.rq = rq;
  }
}
