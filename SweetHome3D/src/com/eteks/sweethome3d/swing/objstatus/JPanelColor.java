package com.eteks.sweethome3d.swing.objstatus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;


public class JPanelColor extends JPanel {

  private List<JPanel> panels = new LinkedList<JPanel>();
  protected Map<String, GridBagConstraints> constraintsPanelMap = new HashMap<String, GridBagConstraints>();
  protected String name;
  
  public JPanelColor(String name)
  {
    this.name = name;
  }
  
  public int getNumberOfRows()
  {
    return this.getPanels().size();
  }
  
  
  
  public void addPanel(JPanel panel, String name)
  {
      panel.setName(name);
      this.addPanel(panel, this.getNumberOfRows(), name);
  }

  
  public void addPanel(JPanel panel, int row, String name)
  {
    panel.setName(name);
    this.addPanel(panel, row, 0);
  }
  
  /**
   * 
   * @param decoratedPanel
   * @param row
   * @param column
   */
  public void addPanel(JPanel panel, int row, int column)
  {
    if(panel.getName() == null || panel.getName().length() == 0)
      throw new IllegalStateException("decoratedPanel must have a name");

    JPanel panelAtRowToAdd = this.getPanelAtRow(row);
    if(panelAtRowToAdd != null)
    {
      shiftDownPanels(row, this.panels.size(), 1);
    }

    GridBagConstraints gc =  new GridBagConstraints
        (/*gridx */column,
            /*gridy*/row, 
            /*gridwidth*/1,
            /*gridheight*/ 1,
            /* weightx */ 0, 
            /* weighty */0,
            /*anchor */ GridBagConstraints.CENTER, 
            /*fill*/ GridBagConstraints.BOTH,
            getDefaultInsets(), 
            /*ipadx*/ 0,
            /*ipady*/0);

    this.addPanel(panel, gc);
  }

  private JPanel getPanelAtRow(int row) {

    try
    {
      return this.getPanels().get(row);
    }
    catch(IndexOutOfBoundsException e)
    {
      return null;
    }

  }

  public int getRowOfPanel(JPanel panel)
  {
    if(panel.getName() == null)
      throw new IllegalStateException("decoratedPanel must have a name!");
    GridBagConstraints c = this.constraintsPanelMap.get(panel.getName());
    if(c == null)
      return -1;
    return c.gridy;
  }



  private void shiftDownPanels(int firstRowAffected, int lastRowAffected, int downOf)
  {
    for(JPanel p : this.getPanels())
    {
      int nowRow = this.getRowOfPanel(p);
      if(nowRow <= lastRowAffected && nowRow >= firstRowAffected)
      {
        nowRow += downOf;
        this.changeRowToPanel(p, nowRow);
      }

    }
  }


  private Insets getDefaultInsets()
  {
    return new Insets(10, 10, 10, 10);
  }

  private void addPanel(JPanel panel, GridBagConstraints constraint)
  {
    this.constraintsPanelMap.put(panel.getName(), constraint);
    this.panels.add(panel);
    this.add(panel, constraint);
  }



  protected void changeRowToPanel(JPanel panel, int newRow)
  {
    GridBagConstraints c = this.constraintsPanelMap.get(panel.getName());
    c.gridy = newRow;
    this.remove(panel);
    this.add(panel, c);
  }



  private List<JPanel> getPanels()
  {
    if(this.panels == null)
      this.panels = new LinkedList<JPanel>();
    return this.panels;
  }

  protected JPanel borderedPanelCenterHorizontal(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    setColor(jp, color);
    return jp;
  }


  protected JPanel borderedPanelCenterVertical(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
    setColor(jp, color);
    return jp;
  }

  private void setColor(JPanel p, Color c)
  {
    p.setBackground(c);
  }



}
