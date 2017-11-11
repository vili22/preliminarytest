package com.preliminary.test;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.plugin.dom.core.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private Map<Integer, BufferedImage> floorPlanImages;

    private Map<Integer, List<Wall>> walls;

    private List<Integer> floorPlanIds;

    private String currentMap = "FirstMap";

    public Model() {

        super("Model");

        initModel();
    }

    private void initModel() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem menuItemOpenProject = new JMenuItem("Open Project");
        menu.add(menuItemOpenProject);
        menuItemOpenProject.addActionListener(e -> loadProject());

        JMenuItem menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.addActionListener(e ->  System.exit(0));
        menu.add(menuItemQuit);

        setJMenuBar(menuBar);

        addMouseKeyListeners();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setResizable(false);
        //setLocationRelativeTo(null);
        setSize(1000,800);
        setVisible(true);
    }

    private void addMouseKeyListeners() {

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                if(imagePanel == null) {
                    return;
                }
                if(e.getKeyCode() == 38) {
                    imagePanel.translateUp();
                } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    imagePanel.translateDown();
                } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    imagePanel.translateLeft();
                }  else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    imagePanel.translateRight();
                } else if(e.getKeyCode() == KeyEvent.VK_A) {
                    imagePanel.zoomIn();
                }else if(e.getKeyCode() == KeyEvent.VK_D) {
                    imagePanel.zoomOut();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void initializeModel(Map<Integer, List<Wall>> walls, Map<Integer, BufferedImage> floorPlanImages) {

        this.floorPlanImages = floorPlanImages;
        this.walls = walls;
        this.floorPlanIds = new ArrayList<>(walls.keySet());
        Collections.sort(floorPlanIds);

        JButton addButton = new JButton("ADD WALL");
        addButton.addActionListener(e -> addNewTask());
        addButton.setFocusable(false);

        JPanel myPanel = new JPanel();
        myPanel.add(addButton);
        myPanel.add(Box.createHorizontalStrut(400));
        myPanel.add(new JLabel("Map"));
        String[] completedList = {"FirstMap", "SecondMap"};
        JComboBox comboBox = new JComboBox(completedList);
        comboBox.setFocusable(false);
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
    }

    private void loadProject() {

        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            Path path = Paths.get(file.getAbsolutePath());
            Path parentFolder = path.getParent();

            String projectFileName = Paths.get(parentFolder.toString(), file.getName()).toString();
            if(!validateProjectFile(projectFileName)) {
                JOptionPane.showMessageDialog(this, "Wrong project file-type should be xml");
                return;
            }
            Map<Integer, List<Wall>> walls = parseXML(projectFileName);
            if(walls.isEmpty()) {
                JOptionPane.showMessageDialog(this, "unable to read any walls from the file");
                return;
            }
            Map<Integer, BufferedImage> floorPlanImages = readMapImages(parentFolder, walls.keySet());
            if(floorPlanImages.isEmpty()) {
                JOptionPane.showMessageDialog(this, "unable to read all floorplan-image corresponding to levels");
                return;
            }
            initializeModel(walls, floorPlanImages);
            setVisible(true);
            repaint();
        }
    }

    public static boolean validateProjectFile(String fileName) {

        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension.equals("xml");
    }


    private void changeMap(int index) {

        imagePanel.changeFloorplan(floorPlanImages.get(floorPlanIds.get(index)), walls.get(floorPlanIds.get(index)));
        setVisible(true);
        repaint();
    }
    private void addNewTask() {

    }

    public static void main(String[] args) {

        Model model = new Model();
    }

    public static Map<Integer, List<Wall>> parseXML(String fileName) {

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

        Map<Integer, Wall> allWalls = new HashMap<>();

        NodeList wallPoints = document.getElementsByTagName("wallPoint");
        int temp = 0;
        while(temp < wallPoints.getLength()) {
            Node nNode = wallPoints.item(temp);
            Element eElement = (Element) nNode;
            int mapId = Integer.parseInt(eElement.getAttribute("mapId"));
            int wallPointId = Integer.parseInt(eElement.getAttribute("id"));
            double x1 = Double.parseDouble(eElement.getAttribute("x"));
            double y1 = Double.parseDouble(eElement.getAttribute("y"));
            if(allWalls.containsKey(wallPointId-1)) {
                allWalls.get(wallPointId - 1).setEndPoint(x1, y1);
            } else {
                Wall wall = new Wall(mapId);
                wall.setStartPoint(x1, y1);
                allWalls.put(wallPointId, wall);
            }
            temp++;
        }

        for(Map.Entry<Integer, Wall> wallEntry : allWalls.entrySet()) {

            if(wallEntry.getValue().isCompleted()) {
                if (!walls.containsKey(wallEntry.getValue().getMapId())) {
                    List<Wall> floorWalls = new ArrayList<>();
                    floorWalls.add(wallEntry.getValue());
                    walls.put(wallEntry.getValue().getMapId(), floorWalls);
                } else {
                    walls.get(wallEntry.getValue().getMapId()).add(wallEntry.getValue());
                }
            }
        }

        return walls;
    }

    private static Map<Integer, BufferedImage> readMapImages(Path parentFolder, Set<Integer> floorIds) {

        Map<Integer, BufferedImage>  floorPlanImages = new HashMap<>();
        Iterator<Integer> iterator = floorIds.iterator();
        while(iterator.hasNext()) {

            int floorPlanId = iterator.next();
            String floorPlan = Paths.get(parentFolder.toString(), "mapimage-" + floorPlanId + ".png").toString();
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(floorPlan));
                floorPlanImages.put(floorPlanId, img);
            } catch (IOException e) {
                e.printStackTrace();
                return new HashMap<>();
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

        private double startx;
        private double starty;
        private double endx;
        private double endy;

        private boolean completed;

        private int mapId;

        public Wall(int mapId) {

            this.mapId = mapId;
        }


        public int getMapId() {

            return mapId;
        }
        public void setStartPoint(double startx, double starty) {

            this.startx = startx;
            this.starty = starty;
        }

        public void  setEndPoint(double endx, double endy) {

            this.endx = endx;
            this.endy = endy;
            this.completed = true;
        }

        public boolean isCompleted() {

            return completed;
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
}
