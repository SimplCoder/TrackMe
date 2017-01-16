package com.trackMe.mapper;

import java.util.Date;

public class Route
{
  private String routeName;
  private String[] locations;
  private String createdBy;
  private String username;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String[] getLocations()
  {
    return this.locations;
  }
  
  public void setLocations(String[] locations)
  {
    this.locations = locations;
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
  
  public String getRouteName()
  {
    return this.routeName;
  }
  
  public void setRouteName(String routeName)
  {
    this.routeName = routeName;
  }
}
