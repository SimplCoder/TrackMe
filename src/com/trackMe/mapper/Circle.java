package com.trackMe.mapper;

class Circle
{
  private double x;
  private double y;
  private int radius;
  
  public Circle(double x, double y, int radius)
  {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  
  public double getX()
  {
    return this.x;
  }
  
  public double getY()
  {
    return this.y;
  }
  
  public int getRadius()
  {
    return this.radius;
  }
}
