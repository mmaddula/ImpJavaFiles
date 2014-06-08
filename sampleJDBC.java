package com.aa.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
 
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
 
public class sampleJDBC
{
  private static void doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
  {
    final JSch jsch = new JSch();
    Session session = jsch.getSession( strSshUser, strSshHost, 22 );
    session.setPassword( strSshPassword );
     
    final Properties config = new Properties();
    config.put( "StrictHostKeyChecking", "no" );
    session.setConfig( config );
     
    session.connect();
    session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
  }
   
  public static void main(String[] args)
  {
    try
    {
      String strSshUser = "ubluv";                  // SSH loging username
      String strSshPassword = "hellboy";                   // SSH login password
      String strSshHost = "192.168.1.7";          // hostname or ip or SSH server
      int nSshPort = 22;                                    // remote SSH host port number
      String strRemoteHost = "localhost";  // hostname or ip of your database server
      int nLocalPort = 3366;                                // local port number use to bind SSH tunnel
      int nRemotePort = 3306;                               // remote port number of your database
      String strDbUser = "mmaddula";                    // database loging username
      String strDbPassword = "hellboy";                    // database login password
      sampleJDBC.doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
       
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort, strDbUser, strDbPassword);
      System.out.println("connection success");
      
      Statement stmt = con.createStatement();
      stmt.executeUpdate("use demo;");
      String sql;
      sql = "SELECT Name FROM GOD";
      ResultSet rs = stmt.executeQuery(sql);
      
      while (rs.next()) {
          int id = rs.getInt(1);
          System.out.println("id=" + id);
         
          if (rs.wasNull()) {
            System.out.println("name is null");
          } else {
            System.out.println("name is not null");
          }
          System.out.println("---------------");
        }
      con.close();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    finally
    {
      System.exit(0);
    }
  }
}