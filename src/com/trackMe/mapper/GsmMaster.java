package com.trackMe.mapper;

import java.util.Date;

public class GsmMaster
{
  private int unitNo;
  private String latitude;
  private String longitude;
  private String location;
  private int speed;
  private int fix;
  private int dir;
  private Date dateTime;
  private int status;
  private String fuel;
  private int ioValues;
  private String idleTime;
  private String distance;
  private String analog2;
  private String digitalIO;
  private String altitude;
  
  public String getIdleTime()
  {
    return this.idleTime;
  }
  
  public void setIdleTime(String idleTime)
  {
    this.idleTime = idleTime;
  }
  
  public String getDistance()
  {
    return this.distance;
  }
  
  public void setDistance(String distance)
  {
    this.distance = distance;
  }
  
  public String getAnalog2()
  {
    return this.analog2;
  }
  
  public void setAnalog2(String analog2)
  {
    this.analog2 = analog2;
  }
  
  public String getDigitalIO()
  {
    return this.digitalIO;
  }
  
  public void setDigitalIO(String digitalIO)
  {
    this.digitalIO = digitalIO;
  }
  
  public int getUnitNo()
  {
    return this.unitNo;
  }
  
  public void setUnitNo(int unitNo)
  {
    this.unitNo = unitNo;
  }
  
  public String getLatitude()
  {
    return this.latitude;
  }
  
  public void setLatitude(String latitude)
  {
    this.latitude = latitude;
  }
  
  public String getLongitude()
  {
    return this.longitude;
  }
  
  public void setLongitude(String longitude)
  {
    this.longitude = longitude;
  }
  
  public String getLocation()
  {
    return this.location;
  }
  
  public void setLocation(String location)
  {
    this.location = location;
  }
  
  public int getSpeed()
  {
    return this.speed;
  }
  
  public void setSpeed(int speed)
  {
    this.speed = speed;
  }
  
  public int getFix()
  {
    return this.fix;
  }
  
  public void setFix(int fix)
  {
    this.fix = fix;
  }
  
  public Date getDateTime()
  {
    return this.dateTime;
  }
  
  public void setDateTime(Date dateTime)
  {
    this.dateTime = dateTime;
  }
  
  public int getStatus()
  {
    return this.status;
  }
  
  public void setStatus(int status)
  {
    this.status = status;
  }
  
  public String getFuel()
  {
    return this.fuel;
  }
  
  public void setFuel(String fuel)
  {
    this.fuel = fuel;
  }
  
  public int getIoValues()
  {
    return this.ioValues;
  }
  
  public void setIoValues(int ioValues)
  {
    this.ioValues = ioValues;
  }
  
  public int getDir()
  {
    return this.dir;
  }
  
  public void setDir(int dir)
  {
    this.dir = dir;
  }
  
  public String getAltitude()
  {
    return this.altitude;
  }
  
  public void setAltitude(String altitude)
  {
    this.altitude = altitude;
  }
}
