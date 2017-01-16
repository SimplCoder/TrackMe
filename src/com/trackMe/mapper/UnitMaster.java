package com.trackMe.mapper;

import java.util.Date;

public class UnitMaster
{
  private int unitNo;
  private String unitName;
  private String gsmNo;
  private String unitIMEINo;
  private boolean onOffStatus;
  private String createdBy;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  
  public int getUnitNo()
  {
    return this.unitNo;
  }
  
  public void setUnitNo(int unitNo)
  {
    this.unitNo = unitNo;
  }
  
  public String getUnitName()
  {
    return this.unitName;
  }
  
  public void setUnitName(String unitName)
  {
    this.unitName = unitName;
  }
  
  public String getGsmNo()
  {
    return this.gsmNo;
  }
  
  public void setGpsNo(String gsmNo)
  {
    this.gsmNo = gsmNo;
  }
  
  public String getUnitIMEINo()
  {
    return this.unitIMEINo;
  }
  
  public void setUnitIMEINo(String unitIMEINo)
  {
    this.unitIMEINo = unitIMEINo;
  }
  
  public boolean getOnOffStatus()
  {
    return this.onOffStatus;
  }
  
  public boolean isOnOffStatus()
  {
    return this.onOffStatus;
  }
  
  public void setOnOffStatus(boolean onOffStatus)
  {
    this.onOffStatus = onOffStatus;
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
