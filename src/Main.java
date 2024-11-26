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
        InitialiseModel model = new InitialiseModel();
        model.createGui();

    }


}