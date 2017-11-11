package com.preliminary.test;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by vvirkkal on 11/11/17.
 */
public class ModelTest {

    @Test
    public void testCorrectProjectFile() {

        String correctFile = "testpath/project.xml";
        assertTrue(Model.validateProjectFile(correctFile));

        String inCorrectFile = "testpath/project.jpg";
        assertFalse(Model.validateProjectFile(inCorrectFile));
    }

    @Test
    public void testTranformCoordinate() {

        double zoom = 1.5;
        int shift = 4;
        double coord = 5;

        int tranformedCoord = ImagePanel.transformCoordinate(coord, zoom, shift);
        assertTrue(tranformedCoord == 11);
    }

}