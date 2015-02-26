package com.eteks.sweethome3d.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

public class JCurtainButton extends JButton
{
  private Set<Action> menuItemsActions = new HashSet<Action>();
  private final JPopupMenu popup = new JPopupMenu();
  
  /**
   * <pre>
   * The action of the father is just for the icon and text,
   * the behaviour should be given by menuItems
   * </pre>
   * @param actionFatherButton
   */
  public JCurtainButton(Action actionFatherButton)
  {
    super(actionFatherButton    );
    this.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             popup.show(e.getComponent(), 0, e.getComponent().getHeight()-2);
         }
     });
  }
  
  public void addMenuItemAction(Action a)
  {
    if( ! this.menuItemsActions.contains(a))
    {
        this.menuItemsActions.add(a);
        popup.add(a);
    }
  }
  
}

