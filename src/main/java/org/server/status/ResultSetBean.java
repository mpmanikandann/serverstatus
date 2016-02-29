package org.server.status;

import java.io.Serializable;

/**
 * Created by Manikandan on 2/27/2016.
 */
public class ResultSetBean implements Serializable {
private String servername;
private String deployedversion;
private String contentswitch;

  public ResultSetBean() {
  }

  public ResultSetBean(String servername, String deployedversion, String contentswitch) {
    this.servername = servername;
    this.deployedversion = deployedversion;
    this.contentswitch = contentswitch;
  }

  public String getServername() {
    return servername;
  }

  public void setServername(String servername) {
    this.servername = servername;
  }

  public String getDeployedversion() {
    return deployedversion;
  }

  public void setDeployedversion(String deployedversion) {
    this.deployedversion = deployedversion;
  }

  public String getContentswitch() {
    return contentswitch;
  }

  public void setContentswitch(String contentswitch) {
    this.contentswitch = contentswitch;
  }

  @Override
  public String toString() {
     return servername+'|'+deployedversion+'|'+contentswitch+'|';
  }
}
