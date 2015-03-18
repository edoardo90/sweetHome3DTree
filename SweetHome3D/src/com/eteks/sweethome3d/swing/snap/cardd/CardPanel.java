package com.eteks.sweethome3d.swing.snap.cardd;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** @see http://stackoverflow.com/questions/5654926 */
public class CardPanel extends JPanel {

    private static final Random random = new Random();
    private static final JPanel cards = new JPanel(new CardLayout());
    private final String name;

    public CardPanel(String name) {
        this.name = name;
        this.setPreferredSize(new Dimension(320, 240));
        this.setBackground(new Color(random.nextInt()));
        this.add(new JLabel(name));
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                create();
            }
        });
    }

    private static void create() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for (int i = 1; i < 9; i++) {
            CardPanel p = new CardPanel("Panel " + String.valueOf(i));
            cards.add(p, p.toString());
        }
        JPanel control = new JPanel();
        control.add(new JButton(new AbstractAction("\u22b2Prev") {

            
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.previous(cards);
            }
        }));
        control.add(new JButton(new AbstractAction("Next\u22b3") {


            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.next(cards);
            }
        }));
        f.add(cards, BorderLayout.CENTER);
        f.add(control, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
