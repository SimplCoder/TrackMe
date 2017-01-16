package com.trackMe.mapper;

import java.util.Date;

public class DriverConf
{
  private int driverId;
  private String vehicleNo;
  private String groupName;
  private String createdBy;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  private String modifyBy;
  private Date modifyDate;
  
  public String getModifiedBy()
  {
    return this.modifiedBy;
  }
  
  public void setModifiedBy(String modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }
  
  public Date getModifiedDate()
  {
    return this.modifiedDate;
  }
  
  public void setModifiedDate(Date modifiedDate)
  {
    this.modifiedDate = modifiedDate;
  }
  
  public int getDriverId()
  {
    return this.driverId;
  }
  
  public void setDriverId(int driverId)
  {
    this.driverId = driverId;
  }
  
  public String getVehicleNo()
  {
    return this.vehicleNo;
  }
  
  public void setVehicleNo(String vehicleNo)
  {
    this.vehicleNo = vehicleNo;
  }
  
  public String getGroupName()
  {
    return this.groupName;
  }
  
  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }
  
  public String getCreatedBy()
  {
    return this.createdBy;
  }
  
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  public Date getCreatedDate()
  {
    return this.createdDate;
  }
  
  public void setCreatedDate(Date createdDate)
  {
    this.createdDate = createdDate;
  }
  
  public String getModifyBy()
  {
    return this.modifyBy;
  }
  
  public void setModifyBy(String modifyBy)
  {
    this.modifyBy = modifyBy;
  }
  
  public Date getModifyDate()
  {
    return this.modifyDate;
  }
  
  public void setModifyDate(Date modifyDate)
  {
    this.modifyDate = modifyDate;
  }
}
