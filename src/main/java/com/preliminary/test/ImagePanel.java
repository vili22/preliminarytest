package com.preliminary.test;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;

    ImagePanel(BufferedImage image) {

        this.image = image;
        //setSize(1000,800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
