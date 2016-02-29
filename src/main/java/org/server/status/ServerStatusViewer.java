package org.server.status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ServerStatusViewer {

  public static void main(String[] args) {
    try {
      //System.out.println(ServerStatusViewer.getResponse("https://premier-uat3.activationnow.com/public/en_US/contentswitch").toString());
      List<ResultSetBean> resultSetBeanList=new LinkedList<>();
      List<String> urllist=new LinkedList<>();
      urllist.add("https://premier-uat.activationnow.com");
      urllist.add("https://premier-uat2.activationnow.com");
      for(String url:urllist){
       ResultSetBean bean=new ResultSetBean();
        bean.setDeployedversion(ServerStatusViewer.getResponse(url+"/version.jsp").toString());
        bean.setContentswitch(ServerStatusViewer.getResponse(url+"/public/en_US/contentswitch").toString());
        bean.setServername(new URL(url).getHost());
        resultSetBeanList.add(bean);
      }
      System.out.println(ServerStatusViewer.getAppVersion());
      /*Map<String,String> drbvalue=ServerStatusViewer.getsncrdrbbundlefromDb("select * from SNF_B2B.SNF_DRB_BUNDLE WHERE BUNDLE='LOAD_BALANCER_SWITCH'","KEY_NAME","VALUE");
      if(!drbvalue.isEmpty()){
        for(Map.Entry<String, String> entry : drbvalue.entrySet()){
        System.out.println(entry.getKey()+"="+entry.getValue());
        }
      }*/
      writefile(resultSetBeanList);

    }catch (IOException e){
      System.err.print(e.toString());
    }
  }

  public static StringBuilder getResponse(String url) throws IOException {
    StringBuilder builder = new StringBuilder();
    URL uri = new URL(url);
    InputStream inputStream = uri.openStream();
    Scanner scanner = new Scanner(inputStream);
    while (scanner.hasNext()) {
      builder.append(scanner.next());
    }
    return builder;
  }

  public static String getAppVersion() throws IOException{
    ResourceBundle bundle=ResourceBundle.getBundle(ServerStatusViewer.class.getName());
    return bundle.getString("Version");
  }

  public static String getBundle(String key) throws IOException{
    ResourceBundle bundle=ResourceBundle.getBundle(ServerStatusViewer.class.getName());
    return bundle.getString(key);

  }

  public static Map<String,String> getsncrdrbbundlefromDb(String sql,String keycolname,String valuecolname){
    Map<String,String> bundlevalues=new HashMap<>();
    ApplicationDatasource datasource=ApplicationDatasource.getdataDatasource();
    try {

      Connection connection=datasource.getConnection(getBundle("db.user"),getBundle("db.password"),getBundle("db.hostname"),getBundle("db.servicename"));
      Statement statement=connection.createStatement();
      boolean executed=statement.execute(sql);
      if(executed){
        ResultSet resultSet=statement.getResultSet();
        while (resultSet.next()){
          bundlevalues.put(resultSet.getString(keycolname),resultSet.getString(valuecolname));
        }
        resultSet.close();
      }
      statement.close();
      connection.close();

    } catch (SQLException |ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bundlevalues;
  }


  public static void writefile(List<ResultSetBean> resultSetBean) throws IOException{
    String filename= "Result_" +System.currentTimeMillis()+".txt";
    System.out.println(filename);
      serilazeFile(filename,resultSetBean);
  }

  public static void serilazeFile(String filename,List<ResultSetBean> resultSetBean) throws IOException {
    FileWriter fileOut=new FileWriter(filename);
    for (ResultSetBean bean:resultSetBean) {
      fileOut.write(bean.toString());
    }

    fileOut.close();
  }


}
