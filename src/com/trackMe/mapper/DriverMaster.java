package com.trackMe.mapper;

import java.util.Date;

public class DriverMaster
{
  private int id;
  private String driverName;
  private String address;
  private String contact1;
  private String contact2;
  private String licenseNo;
  private Date licenseExpiryDate;
  private String rtoName;
  private String bloodGroup;
  private String userName;
  private String createdBy;
  private Date createdDate;
  private String modifiedBy;
  private Date modifiedDate;
  private String driverexp;
  private String remark;
  
  public String getDriverexp()
  {
    return this.driverexp;
  }
  
  public void setDriverexp(String driverexp)
  {
    this.driverexp = driverexp;
  }
  
  public String getRemark()
  {
    return this.remark;
  }
  
  public void setRemark(String remark)
  {
    this.remark = remark;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public String getDriverName()
  {
    return this.driverName;
  }
  
  public void setDriverName(String driverName)
  {
    this.driverName = driverName;
  }
  
  public String getAddress()
  {
    return this.address;
  }
  
  public void setAddress(String address)
  {
    this.address = address;
  }
  
  public String getContact1()
  {
    return this.contact1;
  }
  
  public void setContact1(String contact1)
  {
    this.contact1 = contact1;
  }
  
  public String getContact2()
  {
    return this.contact2;
  }
  
  public void setContact2(String contact2)
  {
    this.contact2 = contact2;
  }
  
  public String getLicenseNo()
  {
    return this.licenseNo;
  }
  
  public void setLicenseNo(String licenseNo)
  {
    this.licenseNo = licenseNo;
  }
  
  public Date getLicenseExpiryDate()
  {
    return this.licenseExpiryDate;
  }
  
  public void setLicenseExpiryDate(Date licenseExpiryDate)
  {
    this.licenseExpiryDate = licenseExpiryDate;
  }
  
  public String getRtoName()
  {
    return this.rtoName;
  }
  
  public void setRtoName(String rtoName)
  {
    this.rtoName = rtoName;
  }
  
  public String getBloodGroup()
  {
    return this.bloodGroup;
  }
  
  public void setBloodGroup(String bloodGroup)
  {
    this.bloodGroup = bloodGroup;
  }
  
  public String getUserName()
  {
    return this.userName;
  }
  
  public void setUserName(String userName)
  {
    this.userName = userName;
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
