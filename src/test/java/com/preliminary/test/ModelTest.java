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
    public void testParseXML() {

        String resource = "project.xml";
        String fileName = ModelTest.class.getClassLoader().getResource(resource).getFile();
        Map<Integer, List<Model.Wall>> walls = Model.parseXML(fileName);
        assertTrue(walls.keySet().size() == 2);
    }

}