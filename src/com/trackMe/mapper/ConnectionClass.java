package com.trackMe.mapper;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionClass
{
  public void getQuery(String sb)
    throws Exception
  {
    Connection c = null;
    Statement stmt = null;
    try
    {
      Class.forName("org.postgresql.Driver");
      c = 
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/trackMe", 
        "trackMe", "root");
      
      stmt = c.createStatement();
      stmt.executeUpdate(sb);
      stmt.close();
      c.close();
    }
    catch (Exception e)
    {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      throw e;
    }
  }
}
