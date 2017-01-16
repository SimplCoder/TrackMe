package com.trackMe.mapper;

import java.lang.reflect.Field;
import java.util.List;

public abstract interface DataObject
{
  public abstract List<Field> getFields();
  
  public abstract String insert();
  
  public abstract String update(String paramString);
  
  public abstract String select(List<String> paramList);
}
