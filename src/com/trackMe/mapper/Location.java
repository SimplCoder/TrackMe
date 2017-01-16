package com.trackMe.mapper;

public class Location
{
  private String latitude;
  private String longitude;
  private String radiusLocation;
  private String radiusReferLocation;
  private String locationName;
  private String locationDescription;
  private String address;
  
  public String getAddress()
  {
    return this.address;
  }
  
  public void setAddress(String address)
  {
    this.address = address;
  }
  
  public String getLatitude()
  {
    return this.latitude;
  }
  
  public void setLatitude(String latitude)
  {
    this.latitude = latitude;
  }
  
  public String getLongitude()
  {
    return this.longitude;
  }
  
  public void setLongitude(String longitude)
  {
    this.longitude = longitude;
  }
  
  public String getRadiusLocation()
  {
    return this.radiusLocation;
  }
  
  public void setRadiusLocation(String radiusLocation)
  {
    this.radiusLocation = radiusLocation;
  }
  
  public String getRadiusReferLocation()
  {
    return this.radiusReferLocation;
  }
  
  public void setRadiusReferLocation(String radiusReferLocation)
  {
    this.radiusReferLocation = radiusReferLocation;
  }
  
  public String getLocationName()
  {
    return this.locationName;
  }
  
  public void setLocationName(String locationName)
  {
    this.locationName = locationName;
  }
  
  public String getLocationDescription()
  {
    return this.locationDescription;
  }
  
  public void setLocationDescription(String locationDescription)
  {
    this.locationDescription = locationDescription;
  }
}
