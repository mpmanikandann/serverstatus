package org.server.status;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Manikandan on 2/25/2016.
 */
public class ApplicationDatasource  {

  private static ApplicationDatasource datasource;

  public static ApplicationDatasource getdataDatasource(){
    return datasource==null?new ApplicationDatasource():datasource;
  }

  Connection getConnection(String username,String password,String hostname, String servicename) throws ClassNotFoundException, SQLException {
    Class.forName("oracle.jdbc.OracleDriver");
    String url="jdbc:oracle:thin:@"+hostname+":1521:"+servicename;
    return DriverManager.getConnection(url,username,password);

  }
  /*OracleDataSource getDataSource(String username,String password,String hostname, String servicename) throws SQLException{
    OracleDataSource dataSource=new OracleDataSource();
    dataSource.setUser(username);
    dataSource.setPassword(password);
    dataSource.setDataSourceName("OracleDatasource");
    dataSource.setDriverType("thin");
    dataSource.setPortNumber(1521);
    dataSource.setDatabaseName(servicename);
    dataSource.setServerName(hostname);

    return dataSource;
  }*/
}
