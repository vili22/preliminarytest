package com.preliminary.test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;

    private List<Model.Wall> initialWalls;

    private int trans_x = 0;
    private int trans_y = 0;

    ImagePanel(BufferedImage image, List<Model.Wall> walls) {

        this.image = image;
        this.initialWalls = walls;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, trans_x, trans_y, this);
        g.setColor(Color.red);
        for(Model.Wall wall : initialWalls) {
            g.drawLine((int) wall.getStartx() + trans_x, (int) wall.getStarty() + trans_y, (int) wall.getEndx() + trans_x, (int) wall.getEndy()+ trans_y);
        }
    }

    public void changeFloorplan(BufferedImage image, List<Model.Wall> walls) {

        this.image = image;
        this.initialWalls = walls;
    }

    public void translateUp() {

        trans_y--;
        repaint();
    }

    public void translateDown() {

        trans_y++;
        repaint();
    }

    public void translateLeft() {

        trans_x--;
        repaint();
    }

    public void translateRight() {

        trans_x++;
        repaint();
    }

    public void zoomIn() {

        System.out.println("zoomin in");
    }

    public void zoomOut() {

        System.out.println("zooming out");
    }
}
