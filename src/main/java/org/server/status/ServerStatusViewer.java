package org.server.status;

import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
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
      String filepath = System.getProperty("input.file");
      List<String> urllist = null;
      if (filepath != null) {
        urllist = ServerStatusViewer.readFile(filepath);
      }
      if (urllist != null) {
        List<ResultSetBean> resultSetBeanList = getResultset(urllist);
        writefile(resultSetBeanList);
      }
    }catch (IOException e){
      System.err.print(e.toString());
    }
  }

  static List<ResultSetBean> getResultset(List<String> urllist) throws IOException {
    List<ResultSetBean> resultSetBeanList = new LinkedList<>();
    for (String url : urllist) {
      try {
        ResultSetBean bean = new ResultSetBean();
        bean.setResponse(ServerStatusViewer.getResponse(url).toString());
        bean.setServername(new URL(url).getHost());
        resultSetBeanList.add(bean);
      } catch (UnknownHostException | SSLHandshakeException | MalformedURLException ex) {
        ResultSetBean bean = new ResultSetBean();
        bean.setResponse(ex.getMessage());
        bean.setServername(url);
        resultSetBeanList.add(bean);
      }
    }
    return resultSetBeanList;
  }

  static StringBuilder getResponse(String url) throws IOException {
    StringBuilder builder = new StringBuilder();
    URL uri = new URL(url);
    InputStream inputStream = uri.openStream();
    Scanner scanner = new Scanner(inputStream);
    while (scanner.hasNext()) {
      builder.append(scanner.next());
    }
    return builder;
  }

  static String getAppVersion() throws IOException {
    ResourceBundle bundle=ResourceBundle.getBundle(ServerStatusViewer.class.getName());
    return bundle.getString("Version");
  }

  static String getBundle(String key) throws IOException {
    ResourceBundle bundle=ResourceBundle.getBundle(ServerStatusViewer.class.getName());
    return bundle.getString(key);

  }

  static Map<String, String> getsncrdrbbundlefromDb(String sql, String keycolname, String valuecolname) {
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

  static void writefile(List<ResultSetBean> resultSetBean) throws IOException {
    String filepath = System.getProperty("user.dir");
    String filename = "Result-"+getAppVersion()+ System.currentTimeMillis() + ".txt";
    File directory = new File(filepath);
    if (!directory.exists()) {
      directory.mkdirs();
      File resultfile = new File(directory, filename);
      if (!resultfile.exists()) {
        resultfile.createNewFile();
        serilazeFile(resultfile, resultSetBean);
      } else {
        serilazeFile(resultfile, resultSetBean);
      }
    } else {
      File resultfile = new File(directory, filename);
      if (!resultfile.exists()) {
        resultfile.createNewFile();
        serilazeFile(resultfile, resultSetBean);
      } else {
        serilazeFile(resultfile, resultSetBean);
      }
    }
  }

  public static void serilazeFile(File filename, List<ResultSetBean> resultSetBean) throws IOException {
    FileWriter outputStream = new FileWriter(filename);
    for (ResultSetBean bean:resultSetBean) {
      outputStream.write(bean.toString());
    }

    outputStream.close();
  }

  public static List<String> readFile(String filepath) throws IOException {
    List<String> urlist = new LinkedList<>();
    File file = new File(filepath);
    if (file.isDirectory()) {
      File dirfiles[] = file.listFiles();
      for (File dir : dirfiles) {
        Scanner scanner = new Scanner(dir);
        while (scanner.hasNext()) {
          urlist.add(scanner.next());
        }
        scanner.close();
      }
    } else {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNext()) {
        urlist.add(scanner.next());
      }
      scanner.close();
    }

    return urlist;
  }

}
