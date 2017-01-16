package com.trackMe.mapper;

import com.trackMe.utilities.Utility;

public class UDPProcessor
  implements Runnable
{
  private String data = null;
  
  public UDPProcessor(String var)
  {
    this.data = var;
  }
  
  public void run()
  {
    Utility utility = new Utility();
    utility.lookup(this.data);
  }
}
