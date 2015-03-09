package com.eteks.sweethome3d.adaptive.security.buildingGraph.policy;

import java.io.Serializable;

public class ABACPolicy implements Serializable {

  private String idAgent;
  private String action;
  private String idResource;
  private String environment;
  
  public ABACPolicy() {
    this.idAgent = "";
    this.action = "";
    this.idResource = "";
    this.environment = "";
  }

  public String getIdAgent() {
    return idAgent;
  }

  public void setIdAgent(String idAgent) {
    this.idAgent = idAgent;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getIdResource() {
    return idResource;
  }

  public void setIdResource(String idResource) {
    this.idResource = idResource;
  }

  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

}
