package com.trackMe.mapper;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DataObjectImpl
  implements DataObject
{
  private Object classObj;
  
  public DataObjectImpl(Object object)
  {
    this.classObj = object;
  }
  
  public List<Field> getFields()
  {
    this.classObj.getClass().getDeclaredFields();
    return Arrays.asList(this.classObj.getClass().getDeclaredFields());
  }
  
  public String insert()
  {
    StringBuilder sql = new StringBuilder(128);
    List<Field> fields = getFields();
    StringBuilder values = new StringBuilder(128);
    sql.append("INSERT INTO ").append(getName());
    sql.append(" (");
    createFieldList(sql, values, getName(), fields);
    sql.append(" ) values ( ").append(values).append(" ); ");
    System.out.println(sql.toString());
    return sql.toString();
  }
  
  public String select(List<String> filters)
  {
    StringBuilder sql = new StringBuilder(128);
    sql.append("SELECT * ").append(getName());
    sql.append(" where ");
    for (String whereClause : filters)
    {
      sql.append(whereClause);
      sql.append(" AND ");
    }
    sql.replace(sql.length() - 5, sql.length(), ";");
    return sql.toString();
  }
  
  public String update(String filters)
  {
    StringBuilder buffer = new StringBuilder(128);
    List<Field> fields = getFields();
    buffer.append("UPDATE ").append(getName());
    
    buffer.append(" set ");
    for (Field field : fields) {
      if (field.getType().isArray())
      {
        buffer.append(field.getName());
        buffer.append("=");
        try
        {
          StringBuffer buffer1 = new StringBuffer();
          Method method = getMethodName(field);
          String[] str = (String[])method.invoke(this.classObj, new Object[0]);
          buffer1.append("'{");
          String[] arrayOfString1;
          int j = (arrayOfString1 = str).length;
          for (int i = 0; i < j; i++)
          {
            String value = arrayOfString1[i];
            
            buffer1.append(value);
            buffer1.append(",");
          }
          buffer1.append("}'");
          buffer.append(buffer1.toString().replace(",}", "}"));
        }
        catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
        {
          e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
          e.printStackTrace();
        }
        buffer.append(",");
      }
      else
      {
        buffer.append(field.getName());
        buffer.append("=");
        buffer.append(getFieldValue(field));
        buffer.append(",");
        if ((field.getType().isInstance(new java.util.Date())) && ((getName().equalsIgnoreCase("GsmMaster")) || (getName().equalsIgnoreCase("GsmStatus"))))
        {
          buffer.append(field.getName() + "date");
          buffer.append("=");
          buffer.append(getDateValue(field));
          buffer.append(",");
        }
      }
    }
    if (filters != null) {
      buffer.append("  where ").append(filters).append(" ; ");
    } else {
      buffer.append(" ; ");
    }
    System.out.println(buffer.toString());
    return buffer.toString();
  }
  
  private Object getFieldValue(Field field)
  {
    try
    {
      Method method = null;
      method = getMethodName(field);
      
      Object value = method.invoke(this.classObj, new Object[0]);
      if (value != null)
      {
        if (field.getType().isInstance(new java.util.Date()))
        {
          value = new Timestamp(((java.util.Date)value).getTime());
          value = "'" + value + "'";
        }
        if (field.getType().isInstance(new String())) {
          value = "'" + value + "'";
        } else if (value.equals("")) {
          return null;
        }
      }
      return value;
    }
    catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException|SecurityException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private Method getMethodName(Field field)
    throws NoSuchMethodException
  {
    Method method;
    
    if (field.getType().getName().equals("boolean")) {
      method = this.classObj.getClass().getMethod("is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[0]);
    } else {
      method = this.classObj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[0]);
    }
    return method;
  }
  
  private Object getDateValue(Field field)
  {
    try
    {
      Method method = this.classObj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), new Class[0]);
      Object value = method.invoke(this.classObj, new Object[0]);
      if (value != null) {
        if (field.getType().isInstance(new java.util.Date())) {
          value = new java.sql.Date(((java.util.Date)value).getTime());
        }
      }
      return "'" + value + "'";
    }
    catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException|SecurityException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private void createFieldList(StringBuilder sb, StringBuilder sb2, String objName, List<Field> fields)
  {
    Iterator<Field> i = fields.iterator();
    boolean first = true;
    while (i.hasNext())
    {
      Field field = (Field)i.next();
      if (first)
      {
        first = false;
      }
      else
      {
        sb.append(",");
        sb2.append(",");
      }
      if ((field.getType().isInstance(new java.util.Date())) && ((getName().equalsIgnoreCase("GsmMaster")) || (getName().equalsIgnoreCase("GsmStatus"))))
      {
        sb.append(field.getName() + "date");
        sb2.append(getDateValue(field));
        sb.append(",");
        sb2.append(",");
        sb.append(field.getName());
        sb2.append(getFieldValue(field));
      }
      else if (field.getType().isArray())
      {
        getArrayValue(sb, sb2, field);
      }
      else
      {
        sb.append(field.getName());
        sb2.append(getFieldValue(field));
      }
    }
  }
  
  private void getArrayValue(StringBuilder sb, StringBuilder sb2, Field field)
  {
    sb.append(field.getName());
    try
    {
      StringBuffer buffer = new StringBuffer();
      Method method = getMethodName(field);
      String[] str = (String[])method.invoke(this.classObj, new Object[0]);
      buffer.append("'{");
      String[] arrayOfString1;
      int j = (arrayOfString1 = str).length;
      for (int i = 0; i < j; i++)
      {
        String value = arrayOfString1[i];
        
        buffer.append(value);
        buffer.append(",");
      }
      buffer.append("}'");
      sb2.append(buffer.toString().replace(",}", "}"));
    }
    catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
  }
  
  private String getName()
  {
    return this.classObj.getClass().getName().substring(this.classObj.getClass().getName().lastIndexOf(".") + 1);
  }
}
