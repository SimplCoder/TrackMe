package com.trackMe.mapper;

import java.util.Date;

public class RouteSchedule
{
  private String scheduleName;
  private Date startDate;
  private String startTime;
  private Date endDate;
  private String endTime;
  private String routeName;
  private String[] vehicleNo;
  private boolean monday;
  private boolean tuesday;
  private boolean wednesday;
  private boolean thursday;
  private boolean friday;
  private boolean saturday;
  private boolean sunday;
  private boolean alertbysms;
  private boolean alertbymail;
  private Date modifiedDate;
  private String modifiedby;
  private Date createdDate;
  private String createdby;
  
  public boolean isMonday()
  {
    return this.monday;
  }
  
  public void setMonday(boolean monday)
  {
    this.monday = monday;
  }
  
  public boolean isTuesday()
  {
    return this.tuesday;
  }
  
  public void setTuesday(boolean tuesday)
  {
    this.tuesday = tuesday;
  }
  
  public boolean isWednesday()
  {
    return this.wednesday;
  }
  
  public void setWednesday(boolean wednesday)
  {
    this.wednesday = wednesday;
  }
  
  public boolean isThursday()
  {
    return this.thursday;
  }
  
  public void setThursday(boolean thursday)
  {
    this.thursday = thursday;
  }
  
  public boolean isFriday()
  {
    return this.friday;
  }
  
  public void setFriday(boolean friday)
  {
    this.friday = friday;
  }
  
  public boolean isSaturday()
  {
    return this.saturday;
  }
  
  public void setSaturday(boolean saturday)
  {
    this.saturday = saturday;
  }
  
  public boolean isSunday()
  {
    return this.sunday;
  }
  
  public void setSunday(boolean sunday)
  {
    this.sunday = sunday;
  }
  
  public boolean isAlertbysms()
  {
    return this.alertbysms;
  }
  
  public void setAlertbysms(boolean alertbysms)
  {
    this.alertbysms = alertbysms;
  }
  
  public boolean isAlertbymail()
  {
    return this.alertbymail;
  }
  
  public void setAlertbymail(boolean alertbymail)
  {
    this.alertbymail = alertbymail;
  }
  
  public Date getModifiedDate()
  {
    return this.modifiedDate;
  }
  
  public void setModifiedDate(Date modifiedDate)
  {
    this.modifiedDate = modifiedDate;
  }
  
  public String getModifiedby()
  {
    return this.modifiedby;
  }
  
  public void setModifiedby(String modifiedby)
  {
    this.modifiedby = modifiedby;
  }
  
  public Date getCreatedDate()
  {
    return this.createdDate;
  }
  
  public void setCreatedDate(Date createdDate)
  {
    this.createdDate = createdDate;
  }
  
  public String getCreatedby()
  {
    return this.createdby;
  }
  
  public void setCreatedby(String createdby)
  {
    this.createdby = createdby;
  }
  
  public void setStartTime(String startTime)
  {
    this.startTime = startTime;
  }
  
  public void setEndTime(String endTime)
  {
    this.endTime = endTime;
  }
  
  public String getScheduleName()
  {
    return this.scheduleName;
  }
  
  public void setScheduleName(String scheduleName)
  {
    this.scheduleName = scheduleName;
  }
  
  public Date getStartDate()
  {
    return this.startDate;
  }
  
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }
  
  public String getStartTime()
  {
    return this.startTime;
  }
  
  public Date getEndDate()
  {
    return this.endDate;
  }
  
  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }
  
  public String getEndTime()
  {
    return this.endTime;
  }
  
  public String getRouteName()
  {
    return this.routeName;
  }
  
  public void setRouteName(String routeName)
  {
    this.routeName = routeName;
  }
  
  public String[] getVehicleNo()
  {
    return this.vehicleNo;
  }
  
  public void setVehicleNo(String[] vehicleNo)
  {
    this.vehicleNo = vehicleNo;
  }
}
