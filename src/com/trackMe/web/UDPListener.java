package com.trackMe.web;

import com.trackMe.mapper.UDPProcessor;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

public class UDPListener
  implements Runnable
{
  public void run()
  {
    System.out.println("I am listening");
    for (;;)
    {
      DatagramSocket datagramSocket = null;
      try
      {
        int port = 8083;
        datagramSocket = new DatagramSocket(port);
        byte[] buffer = new byte[70];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(packet);
        String inputStr = new String(packet.getData(), 0, packet.getLength());
        System.out.println("String received : " + inputStr);
        UDPProcessor myRunnable = new UDPProcessor(inputStr);
        Thread t = new Thread(myRunnable);
      }
      catch (SocketTimeoutException e)
      {
        Thread t;
        e.printStackTrace();
        
        datagramSocket.close(); continue;
      }
      catch (Exception e)
      {
        e.printStackTrace();
        
        datagramSocket.close(); continue;
      }
      finally
      {
        datagramSocket.close();
      }
      datagramSocket.close();
    }
  }
}
