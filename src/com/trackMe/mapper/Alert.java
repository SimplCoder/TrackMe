package com.trackMe.mapper;

import java.util.Date;

public class Alert
{
  private String vehicleNo;
  private boolean overSpeed;
  private boolean suddenBreak;
  private boolean idleTime;
  private boolean panic;
  private boolean geofency;
  private boolean alertBySms;
  private boolean alertByMail;
  private String snoozeTime;
  private String createdBy;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  private boolean group;
  private String contactNo;
  
  public boolean isGroup()
  {
    return this.group;
  }
  
  public void setGroup(boolean group)
  {
    this.group = group;
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
  
  public String getVehicleNo()
  {
    return this.vehicleNo;
  }
  
  public void setVehicleNo(String vehicleNo)
  {
    this.vehicleNo = vehicleNo;
  }
  
  public boolean isOverSpeed()
  {
    return this.overSpeed;
  }
  
  public void setOverSpeed(boolean overSpeed)
  {
    this.overSpeed = overSpeed;
  }
  
  public boolean isSuddenBreak()
  {
    return this.suddenBreak;
  }
  
  public void setSuddenBreak(boolean suddenBreak)
  {
    this.suddenBreak = suddenBreak;
  }
  
  public boolean isIdleTime()
  {
    return this.idleTime;
  }
  
  public void setIdleTime(boolean idleTime)
  {
    this.idleTime = idleTime;
  }
  
  public boolean isPanic()
  {
    return this.panic;
  }
  
  public void setPanic(boolean panic)
  {
    this.panic = panic;
  }
  
  public boolean isGeofency()
  {
    return this.geofency;
  }
  
  public void setGeofency(boolean geofency)
  {
    this.geofency = geofency;
  }
  
  public boolean isAlertBySms()
  {
    return this.alertBySms;
  }
  
  public void setAlertBySms(boolean alertBySms)
  {
    this.alertBySms = alertBySms;
  }
  
  public boolean isAlertByMail()
  {
    return this.alertByMail;
  }
  
  public void setAlertByMail(boolean alertByMail)
  {
    this.alertByMail = alertByMail;
  }
  
  public String getSnoozeTime()
  {
    return this.snoozeTime;
  }
  
  public void setSnoozeTime(String snoozeTime)
  {
    this.snoozeTime = snoozeTime;
  }
  
  public String getContactNo()
  {
    return this.contactNo;
  }
  
  public void setContactNo(String contactNo)
  {
    this.contactNo = contactNo;
  }
}
