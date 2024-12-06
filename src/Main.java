import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the step size");
        //int stepSize = Integer.parseInt(System.console().readLine());
        int stepSize = 100;
        JFrame frame = new JFrame();


        JLayeredPane layeredPane = new JLayeredPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setMinimumSize(new Dimension( 1000, 1000));
        Model model = new Model();
        frame.add(model);
        frame.setVisible(true);
        // TODO remove this
        System.out.println("in main " + model.rays.size());
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    model.update(stepSize);
                }

                frame.repaint();

            }

        });
    }


}