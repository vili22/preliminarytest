package com.preliminary.test;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.plugin.dom.core.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class Model  extends JFrame {

    ImagePanel imagePanel;

    private List<Task> tasks = new ArrayList<>();

    private Map<Integer, BufferedImage> floorPlanImages;

    private Map<Integer, List<Wall>> walls;

    private List<Integer> floorPlanIds;

    private String currentMap = "FirstMap";

    public Model(Map<Integer, List<Wall>> walls, Map<Integer, BufferedImage> floorPlanImages) {

        super("Model");
        this.floorPlanImages = floorPlanImages;
        this.walls = walls;
        this.floorPlanIds = new ArrayList<>(walls.keySet());
        Collections.sort(floorPlanIds);
        initializeModel();
    }

    private void initializeModel() {

        JButton addButton = new JButton("ADD WALL");
        addButton.addActionListener(e -> addNewTask());

        JPanel myPanel = new JPanel();
        myPanel.add(addButton);
        myPanel.add(Box.createHorizontalStrut(400));
        myPanel.add(new JLabel("Map"));
        String[] completedList = {"FirstMap", "SecondMap"};
        JComboBox comboBox = new JComboBox(completedList);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String compStatus = (String)cb.getSelectedItem();
                if(compStatus.equals("FirstMap")) {
                    if(!currentMap.equals("FirstMap")) {
                        currentMap = "FirstMap";
                        changeMap(0);
                    }
                } else {
                    if(!currentMap.equals("SecondMap")) {
                        currentMap = "SecondMap";
                        changeMap(1);
                    }
                }
            }
        });

        myPanel.add(comboBox);

        imagePanel = new ImagePanel(floorPlanImages.get(floorPlanIds.get(0)), walls.get(floorPlanIds.get(0)));

        Container contentPane = getContentPane();
        contentPane.add(myPanel, BorderLayout.PAGE_START);
        contentPane.add(imagePanel, BorderLayout.CENTER);
        //container.add(buttonPane);
        //container.add(imagePanel);
        //add(buttonPane);
        //add(imagePanel);

        //add(container);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setResizable(false);
        //setLocationRelativeTo(null);
        setSize(1000,800);
        setVisible(true);
    }


    private void changeMap(int index) {

        System.out.println("changed to map " + index);
    }
    private void addNewTask() {

//        JTextField descpField = new JTextField(15);
//        JTextField deadLineField = new JTextField(15);
//
//        JPanel myPanel = new JPanel();
//        myPanel.add(new JLabel("Description:"));
//        myPanel.add(descpField);
//        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
//        myPanel.add(new JLabel("Deadline:"));
//        myPanel.add(deadLineField);
//
//        String[] completedList = {"completed", "not completed"};
//        JComboBox comboBox = new JComboBox(completedList);
//        comboBox.setSelectedIndex(0);
//        comboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JComboBox cb = (JComboBox)e.getSource();
//                String compStatus = (String)cb.getSelectedItem();
//                if(compStatus.equals("completed")) {
//                    comboBox.setSelectedIndex(0);
//                } else {
//                    comboBox.setSelectedIndex(1);
//                }
//            }
//        });
//
//        myPanel.add(Box.createHorizontalStrut(15));
//        myPanel.add(comboBox);
//
//        int result = JOptionPane.showConfirmDialog(null, myPanel,
//                "Task description", JOptionPane.OK_CANCEL_OPTION);
//        if (result == JOptionPane.OK_OPTION) {
//            String description = descpField.getText();
//            String deadLine = deadLineField.getText();
//            String completed = completedList[comboBox.getSelectedIndex()];
//            boolean status = completed.equals("completed") ? true : false;
//            tasks.add(new Task(description, status, deadLine));
//            ((DefaultListModel)taskList.getModel()).addElement(description);
//        }
    }

    private void removeTask() {

    }

    private void viewTaskInfo(int index) {

//        Task task = tasks.get(index);
//        String descp = task.getDescription();
//        String deadline = task.getDeadline();
//        JTextField descpField = new JTextField(descp);
//        descpField.setColumns(15);
//
//        JPanel myPanel = new JPanel();
//        myPanel.add(new JLabel("Description: "));
//        myPanel.add(descpField);
//        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
//        myPanel.add(new JLabel("Deadline: " + deadline));
//
//        String[] completedList = {"completed", "not completed"};
//        JComboBox comboBox = new JComboBox(completedList);
//        if(task.isCompleted()) {
//            comboBox.setSelectedIndex(0);
//        } else {
//            comboBox.setSelectedIndex(1);
//        }
//        comboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JComboBox cb = (JComboBox)e.getSource();
//                String compStatus = (String)cb.getSelectedItem();
//                if(compStatus.equals("completed")) {
//                    comboBox.setSelectedIndex(0);
//                } else {
//                    comboBox.setSelectedIndex(1);
//                }
//            }
//        });
//
//        myPanel.add(Box.createHorizontalStrut(15));
//        myPanel.add(comboBox);
//
//        int result = JOptionPane.showConfirmDialog(null, myPanel,
//                "Task description", JOptionPane.OK_CANCEL_OPTION);
//        if(result == JOptionPane.OK_OPTION) {
//            descp = descpField.getText();
//            task.setDescription(descp);
//            ((DefaultListModel)taskList.getModel()).setElementAt(task.getDescription(), index);
//            String completed = completedList[comboBox.getSelectedIndex()];
//            boolean status = completed.equals("completed") ? true : false;
//            task.setCompleted(status);
//        }
    }

    public static void main(String[] args) {

        String filenameDoc = "/home/vvirkkal/Documents/development/misc/eka-hau/ExerciseProject/project.xml";
        String path = "/home/vvirkkal/Documents/development/misc/eka-hau/ExerciseProject/";

        Map<Integer, List<Wall>> walls = parseXML(filenameDoc);
        Map<Integer, BufferedImage> floorPlanImages = readMapImages(path, walls.keySet());

        Model model = new Model(walls, floorPlanImages);
    }

    private static Map<Integer, List<Wall>> parseXML(String fileName) {

        Map<Integer, List<Wall>> walls = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = factory.newDocumentBuilder();
        } catch(Exception e) {
            e.printStackTrace();
            return walls;
        }

        org.w3c.dom.Document document = null;
        try {
            document = db.parse(fileName);
        }catch(Exception e) {
            e.printStackTrace();
            return walls;
        }

        NodeList wallPoints = document.getElementsByTagName("wallPoint");
        int temp = 0;
        while(temp < wallPoints.getLength()) {
            Node nNode = wallPoints.item(temp);
            Wall wall = new Wall();
            Element eElement = (Element) nNode;
            int mapId = Integer.parseInt(eElement.getAttribute("mapId"));
            double x1 = Double.parseDouble(eElement.getAttribute("x"));
            double y1 = Double.parseDouble(eElement.getAttribute("y"));
            wall.setStartPoint(x1, y1);

            temp++;
            nNode = wallPoints.item(temp);
            eElement = (Element) nNode;
            x1 = Double.parseDouble(eElement.getAttribute("x"));
            y1 = Double.parseDouble(eElement.getAttribute("y"));
            wall.setEndPoint(x1, y1);
            if(walls.containsKey(mapId)) {
                walls.get(mapId).add(wall);
            } else {
                List<Wall> floorWalls = new ArrayList<>();
                floorWalls.add(wall);
                walls.put(mapId, floorWalls);
            }
            temp++;
        }
        return walls;
    }

    private static Map<Integer, BufferedImage> readMapImages(String path, Set<Integer> floorIds) {

        Map<Integer, BufferedImage>  floorPlanImages = new HashMap<>();
        Iterator<Integer> iterator = floorIds.iterator();
        while(iterator.hasNext()) {

            int floorPlanId = iterator.next();
            String floorPlan = path + "mapimage-" + floorPlanId + ".png";
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(floorPlan));
                floorPlanImages.put(floorPlanId, img);
            } catch (IOException e) {
                e.printStackTrace();
                return floorPlanImages;
            }
        }
        return floorPlanImages;
    }

    public static class MapModel {

        private Map<Integer, Wall> walls = new HashMap<>();

        MapModel(Map<Integer, Wall> walls) {

            this.walls = walls;
        }
    }

    public static class Wall {

        double startx;
        double starty;
        double endx;
        double endy;

        public Wall() {

        }

        public void setStartPoint(double startx, double starty) {

            this.startx = startx;
            this.starty = starty;
        }

        public void  setEndPoint(double endx, double endy) {

            this.endx = endx;
            this.endy = endy;
        }

        public double getStartx() {
            return startx;
        }

        public void setStartx(double startx) {
            this.startx = startx;
        }

        public double getStarty() {
            return starty;
        }

        public void setStarty(double starty) {
            this.starty = starty;
        }

        public double getEndx() {
            return endx;
        }

        public void setEndx(double endx) {
            this.endx = endx;
        }

        public double getEndy() {
            return endy;
        }

        public void setEndy(double endy) {
            this.endy = endy;
        }
    }

    public static class Task {

        private String description;
        private boolean completed;
        private final String deadline;

        public Task(String description, boolean completed, String deadline ) {

            this.description = description;
            this.completed = completed;
            this.deadline = deadline;
        }

        public void setDescription(String descp) {
            description = descp;
        }

        public String getDescription() {
            return description;
        }

        public void setCompleted(boolean comp) {
            this.completed = comp;
        }

        public boolean isCompleted() {
            return completed;
        }

        public String getDeadline() {
            return deadline;
        }
    }
}
