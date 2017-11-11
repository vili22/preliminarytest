package com.preliminary.test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.*;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;
    private BufferedImage zoomedImage;

    private List<Model.Wall> initialWalls;

    private double zoom = 1.0;

    private int trans_x = 0;
    private int trans_y = 0;

    ImagePanel(BufferedImage image, List<Model.Wall> walls) {

        this.image = image;
        this.zoomedImage = image;
        this.initialWalls = walls;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(zoomedImage, trans_x, trans_y, this);
        g.setColor(Color.red);
        for(Model.Wall wall : initialWalls) {
            g.drawLine((int)(zoom *wall.getStartx()) + trans_x, (int) (zoom * wall.getStarty()) + trans_y, (int) (zoom * wall.getEndx()) + trans_x, (int)(zoom * wall.getEndy())+ trans_y);
        }
    }

    public void changeFloorplan(BufferedImage image, List<Model.Wall> walls) {

        trans_x = 0;
        trans_y = 0;
        zoom = 1.0;
        this.image = image;
        this.zoomedImage = image;
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

        if(zoom < 3) {
            zoom *= 1.1;
            scaleImage(zoom);
            repaint();
        }
    }

    public void zoomOut() {

        if(zoom > 0.3) {
            zoom /= 1.1;
            scaleImage(zoom);
            repaint();
        }
    }

    private void scaleImage(double scale) {

        int w = (int)(scale*image.getWidth());
        int h = (int)(scale*image.getHeight());
        zoomedImage = new BufferedImage(w, h, image.getType());
        Graphics2D g2 = zoomedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        g2.drawRenderedImage(image, at);
        g2.dispose();
    }
}
