package com.eteks.sweethome3d.adaptive.security.buildingGraph.policy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PolicyRules implements Serializable {
    private List<PolicyRule> rules;
    
    public PolicyRules()
    {
      this.rules = new ArrayList<PolicyRule>();
    }

    public List<PolicyRule> getRules() {
      return rules;
    }

    public void addRule(PolicyRule rule)
    {
      this.rules.add(rule);
    }
    
    public void removeRule(PolicyRule rule)
    {
      this.rules.remove(rule);
    }
  
}
