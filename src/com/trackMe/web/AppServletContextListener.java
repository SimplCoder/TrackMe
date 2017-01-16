package com.trackMe.web;

import java.io.PrintStream;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener
  implements ServletContextListener
{
  public void contextDestroyed(ServletContextEvent event)
  {
    System.out.println("Context destroyed ");
  }
  
  public void contextInitialized(ServletContextEvent event)
  {
    System.out.println("Context initialized start");
    UDPListener listener = new UDPListener();
    Thread t = new Thread(listener);
    t.start();
    System.out.println("Context initialized stop");
  }
}
