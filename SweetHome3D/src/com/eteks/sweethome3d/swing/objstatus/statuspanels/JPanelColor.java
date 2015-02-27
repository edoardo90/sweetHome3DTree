package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;


public class JPanelColor extends JPanel {

  private List<JPanelColor> panels = new LinkedList<JPanelColor>();
  protected Map<String, GridBagConstraints> constraintsPanelMap = new HashMap<String, GridBagConstraints>();
  protected String name;
  

  /**
   * <pre>
   * Set the panel name 
   * Set the layout as GridBagLayout
   * </pre>
   * @param name
   */
  public JPanelColor(String name)
  {
    this.setName(name);
    this.name = name;
    this.setLayout(new GridBagLayout());
  }

  /**
   * Each row should be a panel containing something that can return a state
   * @return
   */
  public List<JPanelColor> getPanels()
  {
    if(this.panels == null)
      this.panels = new LinkedList<JPanelColor>();
    return this.panels;
  }


  public int getNumberOfRows()
  {
    return this.getPanels().size();
  }

  /**
   * Append a panel as last row
   * @param panel
   * @param name
   */
  public void addPanel(JPanelColor panel, String name)
  {
    panel.setName(name);
    this.addPanel(panel, this.getNumberOfRows(), name);
  }

  @Override 
  public Component add(Component  c)
  {
    if (c instanceof JPanelColor) {
      JPanelColor colored = (JPanelColor)c;
      this.addPanel(colored, colored.getName());
      return c;
    }
    else
      return super.add(c);
  }

  /**
   * Add a panel at a certain row, pushes down all panels from that row
   * @param panel
   * @param row
   * @param name
   */
  public void addPanel(JPanelColor panel, int row, String name)
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
  public void addPanel(JPanelColor panel, int row, int column)
  {
    if(panel.getName() == null || panel.getName().length() == 0)
      throw new IllegalStateException("decoratedPanel must have a name");

    this.setRandomColor(panel);

    JPanelColor panelAtRowToAdd = this.getPanelAtRow(row);
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
  private void setRandomColor(JPanelColor panel) {
    
    Random rand = new Random();
    float red  = rand.nextFloat();
    float green  = rand.nextFloat();
    float blue  = rand.nextFloat();
    if(blue < 0.7)
      blue += 0.3;
    
    if(green < 0.3)
      green += 0.4;
    
    if(red < 0.5)
      red += 0.5;
    
    
    Color c = new Color(red, green, blue);

    panel.setBackground(c);
  }

  /**
   * 
   * @param row
   * @return  null if the row is free or the JPanelColor at that row
   */
  private JPanelColor getPanelAtRow(int row) {

    try
    {
      return this.getPanels().get(row);
    }
    catch(IndexOutOfBoundsException e)
    {
      return null;
    }

  }

  /**
   * 
   * @param panel
   * @return  the row of the panel or -1 if it is not found
   */
  public int getRowOfPanel(JPanelColor panel)
  {
    if(panel.getName() == null)
      throw new IllegalStateException("decoratedPanel must have a name!");
    GridBagConstraints c = this.constraintsPanelMap.get(panel.getName());
    if(c == null)
      return -1;
    return c.gridy;
  }

  @Override
  public String toString()
  {
    String  s = "Name: " + this.getName() + "\n";
    for(JPanelColor p : this.getPanels())
    {
       s = s + "\t" + "SON: " + p + "\n";
    }
    return s;
  }


  private void shiftDownPanels(int firstRowAffected, int lastRowAffected, int downOf)
  {
    for(JPanelColor p : this.getPanels())
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
    return new Insets(5, 10, 5, 10);
  }

  private void addPanel(JPanelColor panel, GridBagConstraints constraint)
  {
    this.constraintsPanelMap.put(panel.getName(), constraint);
    this.panels.add(panel);
    this.add(panel, constraint);
  }



  protected void changeRowToPanel(JPanelColor panel, int newRow)
  {
    GridBagConstraints c = this.constraintsPanelMap.get(panel.getName());
    c.gridy = newRow;
    this.remove(panel);
    this.add(panel, c);
  }


  protected JPanelColor borderedPanelCenterHorizontal(int bord, Color color)
  {
    JPanelColor jp = new JPanelColor("centHor");
   
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    setColor(jp, color);
    return jp;
  }


  protected JPanelColor borderedPanelCenterVertical(int bord, Color color)
  {
    JPanelColor jp = new JPanelColor("centVer");
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
    setColor(jp, color);
    return jp;
  }

  private void setColor(JPanelColor p, Color c)
  {
    p.setBackground(c);
  }



}
