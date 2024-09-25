import maze1.Maze;
import searchEngine.AStarSearchEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Title:        MazeAStarFirstSearch<p>
 * Description:  Demo program for A* search in a maze<p>
 * Copyright:    Copyright (c) Mark Watson, Released under Open Source Artistic License<p>
 * Company:      Mark Watson Associates<p>
 * @author Mark Watson
 * @version 1.0
 */

public class MazeAStarSearch extends JFrame {
    JPanel jPanel1 = new JPanel();
    AStarSearchEngine currentSearchEngine = null;
    BufferedImage[] images; // contain images

    public MazeAStarSearch() {
        try {
            jbInit();
            loadimages(); // Load images in array
        } catch (Exception e) {
            System.out.println("GUI initialization error: " + e);
        }
        currentSearchEngine = new AStarSearchEngine(10, 10); // Use AStarSearchEngine for the search
        repaint();
    }

    public void paint(Graphics g_unused) {
        if (currentSearchEngine == null) return;
        Maze maze = currentSearchEngine.getMaze();
        int width = maze.getWidth();
        int height = maze.getHeight();
        System.out.println("Size of current maze: " + width + " by " + height);
        Graphics g = jPanel1.getGraphics();
        Graphics g2 = jPanel1.getGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, 320, 320);
        g2.setColor(Color.black);

        maze.setValue(0, 0, Maze.START_LOC_VALUE);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                short val = maze.getValue(x, y);

                if (val == Maze.OBSTICLE) {
                    g2.drawImage(images[0], 6 + x * 29, 3 + y * 29, null); // Draw obstacle

                } else if (val == Maze.START_LOC_VALUE) {
                    g2.drawImage(images[1], 6 + x * 29, 3 + y * 29, null); // Draw start location

                } else if (val == Maze.GOAL_LOC_VALUE) {
                    g2.drawImage(images[2], 6 + x * 29, 3 + y * 29, null); // Draw goal location

                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(6 + x * 29, 3 + y * 29, 29, 29);
                    g2.setColor(Color.BLUE);
                    g2.drawRect(6 + x * 29, 3 + y * 29, 29, 29);
                }
            }
        }
        Dimension goal = maze.goalLoc;
        g.drawImage(images[2], 6 + goal.width * 29, 3 + goal.height * 29, null); // Draw goal

        // Redraw the path in black:
        g2.setColor(Color.black);
        Dimension[] path = currentSearchEngine.getPath();
        for (int i = 1; i < path.length - 1; i++) {
            int x = path[i].width;
            int y = path[i].height;
            short val = maze.getValue(x, y);
            g2.drawString("" + (path.length - i), 16 + x * 29, 19 + y * 29); // Draw path steps
        }
    }

    public static void main(String[] args) {
        MazeAStarSearch mazeSearch1 = new MazeAStarSearch(); // Instantiate A* search demo
    }

    private void loadimages() {
        try {
            images = new BufferedImage[4];
            images[0] = ImageIO.read(new File("src/images/brick.png"));    // Obstacle
            images[1] = ImageIO.read(new File("src/images/monster.png"));  // Start location
            images[2] = ImageIO.read(new File("src/images/pacman.png"));   // Goal location
        } catch (IOException ex) {
            Logger.getLogger(MazeAStarSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jbInit() throws Exception {
        this.setContentPane(jPanel1);
        this.setCursor(null);
        this.setDefaultCloseOperation(3);
        this.setTitle("MazeAStarFirstSearch");
        this.getContentPane().setLayout(null);
        jPanel1.setBackground(Color.white);
        jPanel1.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
        jPanel1.setDoubleBuffered(false);
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(null);
        this.setSize(320, 340);
        this.setVisible(true);
    }
}
