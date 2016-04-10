package org.server.status;

import java.io.Serializable;

/**
 * Created by Manikandan on 2/27/2016.
 */
public class ResultSetBean implements Serializable {
private String servername;
private String response;

  public ResultSetBean() {
  }

  public ResultSetBean(String servername, String response, String contentswitch) {
    this.servername = servername;
    this.response = response;
  }

  public String getServername() {
    return servername;
  }

  public void setServername(String servername) {
    this.servername = servername;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  @Override
  public String toString() {
     return servername + '|' + response;
  }
}
