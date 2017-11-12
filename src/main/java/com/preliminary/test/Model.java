package com.preliminary.test;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by vvirkkal on 11/10/17.
 */
public class Model  extends JFrame {

    ImagePanel imagePanel;

    private Map<String, BufferedImage> floorPlanImages = new HashMap<>();

    private Map<String, List<Wall>> walls;

    private String currentMap;

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
        menuItemQuit.addActionListener(e -> System.exit(0));
        menu.add(menuItemQuit);

        setJMenuBar(menuBar);

        addMouseKeyListeners();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setResizable(false);
        //setLocationRelativeTo(null);
        setSize(1000, 800);
        setVisible(true);
    }

    private void addMouseKeyListeners() {

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (imagePanel == null) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    imagePanel.translateDown();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    imagePanel.translateUp();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    imagePanel.translateRight();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    imagePanel.translateLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    imagePanel.zoomIn();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    imagePanel.zoomOut();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void initializeModel(ProjectStructure projectStructure) {

        this.floorPlanImages = projectStructure.getFloorPlanImages();
        this.walls = projectStructure.getWalls();
        this.currentMap = projectStructure.getFloorPlanName(0);

        JButton addButton = new JButton("ADD WALL");
        addButton.addActionListener(e -> addWall());
        addButton.setFocusable(false);

        JPanel myPanel = new JPanel();
        myPanel.add(addButton);
        myPanel.add(Box.createHorizontalStrut(400));
        myPanel.add(new JLabel("Map"));
        String[] completedList = {projectStructure.getFloorPlanName(0), projectStructure.getFloorPlanName(1)};
        JComboBox comboBox = new JComboBox(completedList);
        comboBox.setFocusable(false);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            String compMapName = (String) cb.getSelectedItem();
            if (!currentMap.equals(compMapName)) {
                currentMap = compMapName;
                changeMap(currentMap);
            }
        });

        myPanel.add(comboBox);

        imagePanel = new ImagePanel(floorPlanImages.get(projectStructure.getFloorPlanName(0)), walls.get(projectStructure.getFloorPlanName(0)));

        Container contentPane = getContentPane();
        contentPane.add(myPanel, BorderLayout.PAGE_START);
        contentPane.add(imagePanel, BorderLayout.CENTER);
    }

    private void loadProject() {

        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Zip-filter", "zip"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            ProjectStructure projectStructure = null;
            try {
                projectStructure = loadProjectFromZipFile(file.getAbsolutePath());
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this, "Unable to parse project file. Error " + e.getMessage());
                return;
            }

            initializeModel(projectStructure);
            setVisible(true);
            repaint();
        }
    }

    public static boolean checkExtension(String fileName, String testExtension) {

        int i = fileName.lastIndexOf('.');
        String extension  = null;
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        return extension != null ? extension.equals(testExtension) : false;
    }

    private static int  parseFloorPlanId(String fileName) {

        int start = fileName.lastIndexOf("-");
        int end = fileName.lastIndexOf('.');

        String floorPlanId = fileName.substring(start +1, end);
        return Integer.parseInt(floorPlanId);
    }


    private void changeMap(String mapName) {

        imagePanel.changeFloorplan(floorPlanImages.get(mapName), walls.get(mapName));
        setVisible(true);
        repaint();
    }

    private void addWall() {

    }

    public static void main(String[] args) {

        Model model = new Model();

    }

    private static ProjectStructure loadProjectFromZipFile(String zipFileName) throws Exception {

        ZipFile zip = new ZipFile(zipFileName);

        ProjectStructure projectStructure = null;
        Map<Integer, BufferedImage> parsedImages = new HashMap<>();

        Enumeration<? extends ZipEntry> entries = zip.entries();
        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (checkExtension(entry.getName(), "png")) {
                BufferedImage image = ImageIO.read(zip.getInputStream(entry));
                parsedImages.put(parseFloorPlanId(entry.getName()), image);
            } else if (checkExtension(entry.getName(), "xml")) {
                projectStructure = parseXML(zip.getInputStream(entry));
            }
        }

        if(projectStructure != null) {
            projectStructure.setFloorPlanImages(parsedImages);
        }
        return projectStructure;
    }

    public static ProjectStructure parseXML(InputStream inputStream) throws Exception{

        ProjectStructure projectStructure = new ProjectStructure();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        db = factory.newDocumentBuilder();

        org.w3c.dom.Document document = null;
        document = db.parse(inputStream);

        Map<Integer, String> floorPlanNames = new HashMap<>();
        NodeList floorPlans = document.getElementsByTagName("map");
        for(int k = 0; k < floorPlans.getLength(); k++) {
            Node nNode = floorPlans.item(k);
            Element eElement = (Element) nNode;
            int id = Integer.parseInt(eElement.getAttribute("id"));
            String name = eElement.getAttribute("name");
            floorPlanNames.put(id, name);

        }


        projectStructure.setFloorPlanIdToNameMap(floorPlanNames);

        Map<String, List<Wall>> walls = new HashMap<>();
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
                Wall wall = new Wall(floorPlanNames.get(mapId));
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

        projectStructure.setWalls(walls);
        return projectStructure;
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

    public static class ProjectStructure {

        private Map<Integer, String> floorPlanIdToNameMap;
        private Map<String, List<Wall>> walls;
        private Map<String, BufferedImage> floorPlanImages;
        private List<Integer> orderedIds;

        public ProjectStructure() {}

        public void setFloorPlanIdToNameMap(Map<Integer, String> floorPlanIdToNameMap) {

            this.floorPlanIdToNameMap = floorPlanIdToNameMap;
        }

        public void setWalls(Map<String, List<Wall>> walls) {

            this.walls = walls;
        }

        public Map<String, List<Wall>> getWalls() {

            return walls;
        }

        public Map<String, BufferedImage> getFloorPlanImages() {

            return floorPlanImages;
        }

        public void setFloorPlanImages(Map<Integer, BufferedImage> images) {

            this.floorPlanImages = new HashMap<>();
            for(Map.Entry<Integer, BufferedImage> entry : images.entrySet()) {
                floorPlanImages.put(floorPlanIdToNameMap.get(entry.getKey()), entry.getValue());
            }

            orderedIds = new ArrayList<>(images.keySet());
            Collections.sort(orderedIds);
        }

        public List<Integer> getOrderedIds() {

            return orderedIds;
        }

        public String getFloorPlanName(int index) {

            return floorPlanIdToNameMap.get(orderedIds.get(index));
        }
    }

    public static class Wall {

        private double startx;
        private double starty;
        private double endx;
        private double endy;

        private boolean completed;

        private String mapId;

        public Wall(String mapId) {

            this.mapId = mapId;
        }


        public String getMapId() {

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
