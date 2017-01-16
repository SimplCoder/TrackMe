package com.trackMe.mapper;

import java.util.Date;

public class Movement
{
  private String vehicle;
  private String snoozetime;
  private Date startdate;
  private Date enddate;
  private String startTime;
  private String endTime;
  private boolean groups;
  private String groupname;
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
  private String id;
  private String modifiedby;
  
  public String getVehicle()
  {
    return this.vehicle;
  }
  
  public void setVehicle(String vehicle)
  {
    this.vehicle = vehicle;
  }
  
  public String getSnoozetime()
  {
    return this.snoozetime;
  }
  
  public void setSnoozetime(String snoozetime)
  {
    this.snoozetime = snoozetime;
  }
  
  public Date getStartdate()
  {
    return this.startdate;
  }
  
  public void setStartdate(Date startdate)
  {
    this.startdate = startdate;
  }
  
  public Date getEnddate()
  {
    return this.enddate;
  }
  
  public void setEnddate(Date enddate)
  {
    this.enddate = enddate;
  }
  
  public String getStartTime()
  {
    return this.startTime;
  }
  
  public void setStartTime(String startTime)
  {
    this.startTime = startTime;
  }
  
  public String getEndTime()
  {
    return this.endTime;
  }
  
  public void setEndTime(String endTime)
  {
    this.endTime = endTime;
  }
  
  public boolean isGroups()
  {
    return this.groups;
  }
  
  public void setGroups(boolean group)
  {
    this.groups = group;
  }
  
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
  
  public String getGroupname()
  {
    return this.groupname;
  }
  
  public void setGroupname(String groupname)
  {
    this.groupname = groupname;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public void setId(String id)
  {
    this.id = id;
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
}
