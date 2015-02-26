package com.eteks.sweethome3d.junit.adaptive.swing;

import javax.swing.*;

import com.eteks.sweethome3d.swing.ActionCoolFactory;
import com.eteks.sweethome3d.swing.JCurtainButton;
import com.eteks.sweethome3d.swing.filter.JFilterButton;
import com.eteks.sweethome3d.viewcontroller.HomeView.ActionType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class ToolbarDemo {

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(600, 400));
        final JToolBar toolBar = new JToolBar();

        //Create the popup menu.
        final JPopupMenu popup = new JPopupMenu();
        
        popup.add(new JMenuItem(new AbstractAction("Option 1") {
            public void actionPerformed(ActionEvent e) {
                 System.out.println("Option 1 selected");
            }
        }));
        popup.add(new JMenuItem(new AbstractAction("Option 2") {
            public void actionPerformed(ActionEvent e) {
               System.out.println("Option 2 selected");
            }
        }));

        final JButton button = new JButton("Options");
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        toolBar.add(button);
        

        
        JCurtainButton b2 = new JFilterButton();
        Action a=null;
        
        b2.addMenuItemAction(a);  
        toolBar.add(b2);
        
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    
    
    
    
}
