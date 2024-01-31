package TP2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameRender extends JPanel {
    private Dungeon dungeon;
    private Hero hero;
    private int framerate;
    private long startTime;
    
    public GameRender(Dungeon dungeon, DynamicThings hero) {
        this.dungeon = dungeon;
        this.hero = Hero.getInstance();
        
     // Initialize frameCount and startTime
        framerate = 0;
        startTime = System.nanoTime();

        // Set up a Timer to update and display the average FPS every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Calculate elapsed time
                long currentTime = System.nanoTime();
                double elapsedTime = (currentTime - startTime) / 1_000_000_000.0;

                // Calculate and display the average FPS
                int currentFrameCount = framerate;
                double averageFPS = currentFrameCount / elapsedTime;
                System.out.println("Average FPS: " + averageFPS);

                // Reset frameCount and update startTime
                framerate = 0;
                startTime = currentTime;
            }
        });
        timer.start();

        // Set up a Timer to repaint the panel at a fixed rate (e.g., 60 times per second)
        Timer repaintTimer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        repaintTimer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Things t : dungeon.getRenderList()){
            t.draw(g);
        }
        hero.draw(g);
        framerate++;
    }
    
    public double getFramerate(){
        long currentTime = System.nanoTime();
        double elapsedTime = (currentTime - startTime) / 1_000_000_000.0;
        int currentFrameCount = framerate;
        double averageFPS = currentFrameCount / elapsedTime;

        // Reset frameCount and update startTime
        framerate = 0;
        startTime = currentTime;

        return averageFPS;
    }
    
    public void resetFramerate(){
    	framerate = 0;
    }
    
}
