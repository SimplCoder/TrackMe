package com.trackMe.mapper;

import java.util.Date;

public class VehicleService
{
  private int serviceId;
  private String vehicleNo;
  private String driverName;
  private Date serviceDate;
  private String fromTime;
  private String toTime;
  private String serviceLocation;
  private String serviceStation;
  private String serviceDesc;
  private String componentDesc;
  private String billNo;
  private int amount;
  private int currentOdo;
  private int nextOdo;
  
  public int getServiceId()
  {
    return this.serviceId;
  }
  
  public void setServiceId(int serviceId)
  {
    this.serviceId = serviceId;
  }
  
  public String getVehicleNo()
  {
    return this.vehicleNo;
  }
  
  public void setVehicleNo(String vehicleNo)
  {
    this.vehicleNo = vehicleNo;
  }
  
  public String getDriverName()
  {
    return this.driverName;
  }
  
  public void setDriverName(String driverName)
  {
    this.driverName = driverName;
  }
  
  public Date getServiceDate()
  {
    return this.serviceDate;
  }
  
  public void setServiceDate(Date serviceDate)
  {
    this.serviceDate = serviceDate;
  }
  
  public String getFromTime()
  {
    return this.fromTime;
  }
  
  public void setFromTime(String fromTime)
  {
    this.fromTime = fromTime;
  }
  
  public String getToTime()
  {
    return this.toTime;
  }
  
  public void setToTime(String toTime)
  {
    this.toTime = toTime;
  }
  
  public String getLocation()
  {
    return this.serviceLocation;
  }
  
  public void setLocation(String serviceLocation)
  {
    this.serviceLocation = serviceLocation;
  }
  
  public String getServiceStation()
  {
    return this.serviceStation;
  }
  
  public void setServiceStation(String serviceStation)
  {
    this.serviceStation = serviceStation;
  }
  
  public String getServiceDesc()
  {
    return this.serviceDesc;
  }
  
  public void setServiceDesc(String serviceDesc)
  {
    this.serviceDesc = serviceDesc;
  }
  
  public String getComponentDesc()
  {
    return this.componentDesc;
  }
  
  public void setComponentDesc(String componentDesc)
  {
    this.componentDesc = componentDesc;
  }
  
  public String getBillNo()
  {
    return this.billNo;
  }
  
  public void setBillNo(String billNo)
  {
    this.billNo = billNo;
  }
  
  public int getAmount()
  {
    return this.amount;
  }
  
  public void setAmount(int amount)
  {
    this.amount = amount;
  }
  
  public int getCurrentOdo()
  {
    return this.currentOdo;
  }
  
  public void setCurrentOdo(int currentOdo)
  {
    this.currentOdo = currentOdo;
  }
  
  public int getNextOdo()
  {
    return this.nextOdo;
  }
  
  public void setNextOdo(int nextOdo)
  {
    this.nextOdo = nextOdo;
  }
}
