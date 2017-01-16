package com.trackMe.web;

import java.io.PrintStream;

public class Mainclass
{
  public static void main(String[] args)
  {
    String formula = "0.5371x - 69.941";
    formula = formula.replace("x", "(2)");
    System.out.println(formula);
  }
}
