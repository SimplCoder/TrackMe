package com.trackMe.mapper;

import java.util.Date;

public class FuelDetail
{
  private int tranId;
  private String vehicleNo;
  private String driverName;
  private Date fillDate;
  private String location;
  private String stationName;
  private int currentOdo;
  private int fuelPerLitre;
  private int fuelQty;
  private String billNo;
  
  public int getTranId()
  {
    return this.tranId;
  }
  
  public void setTranId(int tranId)
  {
    this.tranId = tranId;
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
  
  public Date getFillDate()
  {
    return this.fillDate;
  }
  
  public void setFillDate(Date fillDate)
  {
    this.fillDate = fillDate;
  }
  
  public String getLocation()
  {
    return this.location;
  }
  
  public void setLocation(String location)
  {
    this.location = location;
  }
  
  public String getStationName()
  {
    return this.stationName;
  }
  
  public void setStationName(String stationName)
  {
    this.stationName = stationName;
  }
  
  public int getCurrentOdo()
  {
    return this.currentOdo;
  }
  
  public void setCurrentOdo(int currentOdo)
  {
    this.currentOdo = currentOdo;
  }
  
  public int getFuelPerLitre()
  {
    return this.fuelPerLitre;
  }
  
  public void setFuelPerLitre(int fuelPerLitre)
  {
    this.fuelPerLitre = fuelPerLitre;
  }
  
  public int getFuelQty()
  {
    return this.fuelQty;
  }
  
  public void setFuelQty(int fuelQty)
  {
    this.fuelQty = fuelQty;
  }
  
  public String getBillNo()
  {
    return this.billNo;
  }
  
  public void setBillNo(String billNo)
  {
    this.billNo = billNo;
  }
}
