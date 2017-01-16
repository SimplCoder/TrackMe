package com.trackMe.mapper;

public class GeoFancyMaster
{
  private String zone;
  private String type;
  private String name;
  private int latitude;
  private int longitude;
  
  public String getZone()
  {
    return this.zone;
  }
  
  public void setZone(String zone)
  {
    this.zone = zone;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int getLatitude()
  {
    return this.latitude;
  }
  
  public void setLatitude(int latitude)
  {
    this.latitude = latitude;
  }
  
  public int getLongitude()
  {
    return this.longitude;
  }
  
  public void setLongitude(int longitude)
  {
    this.longitude = longitude;
  }
}
