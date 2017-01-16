package com.trackMe.mapper;

import java.io.PrintStream;

public class MainClass
{
  public static void main(String[] args)
  {
    Circle circle = new Circle(12.97123D, 77.714838D, 1000);
    System.out.println(calculateDistance(circle.getX(), circle.getY(), 12.956947D, 77.701535D) < circle.getRadius());
  }
  
  static double calculateDistance(double longitude1, double latitude1, double longitude2, double latitude2)
  {
    double c = 
    
      Math.sin(Math.toRadians(latitude1)) * 
      
      Math.sin(Math.toRadians(latitude2)) + 
      
      Math.cos(Math.toRadians(latitude1)) * 
      
      Math.cos(Math.toRadians(latitude2)) * 
      
      Math.cos(Math.toRadians(longitude2) - 
      
      Math.toRadians(longitude1));
    
    c = c > 0.0D ? Math.min(1.0D, c) : Math.max(-1.0D, c);
    
    return 6370031.0D * Math.acos(c);
  }
}
