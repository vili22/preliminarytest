package com.preliminary.test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;

    private List<Model.Wall> initialWalls;

    ImagePanel(BufferedImage image, List<Model.Wall> walls) {

        this.image = image;
        this.initialWalls = walls;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        g.setColor(Color.red);
        for(Model.Wall wall : initialWalls) {
            g.drawLine((int) wall.getStartx(), (int) wall.getStarty(), (int) wall.getEndx(), (int) wall.getEndy());
        }
    }

    public void changeFloorplan(BufferedImage image, List<Model.Wall> walls) {

        this.image = image;
        this.initialWalls = walls;
    }
}
