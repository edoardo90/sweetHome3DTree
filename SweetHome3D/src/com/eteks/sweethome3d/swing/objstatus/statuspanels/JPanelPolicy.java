package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.ABACPolicy;
import com.eteks.sweethome3d.swing.objstatus.tables.PanelWithTable;
import com.eteks.sweethome3d.swing.objstatus.tables.TableListModel;

public class JPanelPolicy extends PanelWithTable<ABACPolicy> {

  private static final long serialVersionUID = 6975346991912795050L;

  private JButton btnAddRow;

  public JPanelPolicy(List<ABACPolicy> policies) {
    super( new TablePolicyModel(policies));
    addComps();
  }

  private void addComps()
  {

    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(box);


    this.btnAddRow = new JButton("Add a new Policy");
    this.btnAddRow.setAlignmentX(CENTER_ALIGNMENT);
    this.btnAddRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        TablePolicyModel tpm = (TablePolicyModel) table.getModel();
        tpm.addRow(new ABACPolicy());
        table.repaint();
      }
    });
    this.add(btnAddRow);
  }


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
      return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
      return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
      boolean DEBUG = true;
      if (DEBUG) {
        System.out.println("Setting value at " + row + "," + col
            + " to " + value
            + " (an instance of "
            + value.getClass() + ")");
      }
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
