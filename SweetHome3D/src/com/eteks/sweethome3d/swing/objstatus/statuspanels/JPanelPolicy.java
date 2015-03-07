package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.ABACPolicy;
import com.eteks.sweethome3d.swing.objstatus.tables.JTablePredefinedValues;
import com.eteks.sweethome3d.swing.objstatus.tables.PanelWithTable;
import com.eteks.sweethome3d.swing.objstatus.tables.TableListModel;

public class JPanelPolicy extends PanelWithTable<ABACPolicy> {

  public JPanelPolicy(List<ABACPolicy> policies) {
    super( new TablePolicyModel(policies));
  }



  private static final long serialVersionUID = 6975346991912795050L;

  private ABACPolicy policy;
  private JTablePredefinedValues table;



  public void setIdAgents(List<String> idAgents)
  {

  }

  public static class TablePolicyModel extends TableListModel<ABACPolicy>
  {
    private static final long serialVersionUID = 606068189328246701L;

    public TablePolicyModel(List<ABACPolicy> rows) {
      super(rows, getPolicyHeaders());

    }

    private static List<String> getPolicyHeaders()
    {
      List<String> colsHeaderL = new ArrayList<String>();
      colsHeaderL.add("Agent Id");
      colsHeaderL.add("Action");
      colsHeaderL.add("Resource");
      colsHeaderL.add("Environment");
      return colsHeaderL;
    }


    @Override
    public Object getValueAt(int row, int col) {

      ABACPolicy policyRow = this.getRowAt(row);
      switch (col) {
        case 0:
          return policyRow.getIdAgent();
        case 1:
          return policyRow.getAction();
        case 2:
          return policyRow.getIdResource();
        case 3:
          return policyRow.getEnvironment();
        default:
          return null;

      }

    }

    @Override
    protected String niceString(String s) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
      return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
      ABACPolicy policyRow = this.getRowAt(row); 
      switch (col) {
        case 0:
          policyRow.setIdAgent((String)value);
          break;
        case 1:
          policyRow.setAction((String)value);
          break;
        case 2:
          policyRow.setIdResource((String)value);
          break;
        case 3:
          policyRow.setEnvironment((String)value);
          break;          
          
        default:
          break;
      }

    }

  }

}
