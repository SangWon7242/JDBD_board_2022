package com.psw.exam.board.controller;

import com.psw.exam.board.Container;
import com.psw.exam.board.Rq;

import java.util.Scanner;

public abstract class Controller {
  protected Scanner sc;

  public Controller() {
    this.sc = Container.scanner;
  }
}
