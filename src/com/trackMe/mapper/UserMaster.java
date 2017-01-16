package com.trackMe.mapper;

import java.util.Date;

public class UserMaster
{
  private String userName;
  private String password;
  private String level;
  private String mapView;
  private String createdBy;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  
  public String getUserName()
  {
    return this.userName;
  }
  
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getLevel()
  {
    return this.level;
  }
  
  public void setLevel(String level)
  {
    this.level = level;
  }
  
  public String getMapView()
  {
    return this.mapView;
  }
  
  public void setMapView(String mapView)
  {
    this.mapView = mapView;
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
}
