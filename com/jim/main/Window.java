package com.jim.main;
import java.awt.Dimension;
import javax.swing.JFrame;
public class Window {
    public Window(int w, int h, String s, Game game) {
        game.setPreferredSize(new Dimension(w, h));
        game.setMaximumSize(new Dimension(w, h));
        game.setMinimumSize(new Dimension(w, h));
        JFrame jf = new JFrame(s);
        jf.add(game);
        jf.pack();
        jf.setDefaultCloseOperation(3);
        jf.setResizable(false);
        jf.setVisible(true);
        game.start();
    }
}
