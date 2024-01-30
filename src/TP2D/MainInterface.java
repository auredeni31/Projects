package TP2D;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainInterface extends JFrame implements KeyListener {
    private TileManager tileManager = new TileManager(48, 48, "./img/tileSet.png");
    private Dungeon dungeon = new Dungeon("./gameData/level1.txt", tileManager);
    private Hero hero = Hero.getInstance();
    private GameRender panel = new GameRender(dungeon, hero);
    private StartScreen startScreen;
    private boolean gameOver = false;
	protected Frame framerate = new Frame("Test");

    public MainInterface() throws HeadlessException {
        super();
        initializeUI();
        initializeKeyListener();
    }

    private void initializeUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(dungeon.getWidth() * tileManager.getWidth(), dungeon.getHeight() * tileManager.getHeigth()));

        startScreen = new StartScreen(this);
        add(startScreen);

        ActionListener animationTimer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {


                    repaint();
                    final int speed = 20;
                    if (hero.isWalking()) {
                        switch (hero.getOrientation()) {
                            case LEFT:
                                hero.moveIfPossible(-speed, 0, dungeon);
                                break;
                            case RIGHT:
                                hero.moveIfPossible(speed, 0, dungeon);
                                break;
                            case UP:
                                hero.moveIfPossible(0, -speed, dungeon);
                                break;
                            case DOWN:
                                hero.moveIfPossible(0, speed, dungeon);
                                break;
                        }
                    }

                    if (hero.getHealth() <= 0) {
                        gameOver = true;
                        displayGameOver();
                    }
                }
            }
        };
        Timer timer = new Timer(50, animationTimer);
        timer.start();
        
        // Marche pas pour l'instant, je me suis inspiré de ca : https://openclassrooms.com/forum/sujet/afficher-le-nombre-de-fps-77567
        Timer framerate_timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //Affichage des fps
                framerate.setTitle("FPS actuels : " + panel.getFramerate());
                //Remise à 0 des fps
                panel.resetFramerate();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void initializeKeyListener() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public void startGame() {
        startScreen.setVisible(false);
        remove(startScreen);
        add(panel);
        setVisible(true);
        framerate.setVisible(true);
        this.playMusic("./sound/musique.wav");
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                hero.setOrientation(Orientation.LEFT);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_RIGHT:
                hero.setOrientation(Orientation.RIGHT);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_UP:
                hero.setOrientation(Orientation.UP);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_DOWN:
                hero.setOrientation(Orientation.DOWN);
                hero.setWalking(true);
                break;
            case KeyEvent.VK_P:
                // Appeler la méthode pour infliger des dégâts lorsque la touche "P" est enfoncée
                inflictDamage(50);
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        hero.setWalking(false);
    }

    // Méthode pour infliger des dégâts au héros
    private void inflictDamage(int damage) {
        hero.takeDamage(damage);
    }

    private void displayGameOver() {
        JOptionPane.showMessageDialog(this, "Mission Failed", "Try again HABIBI !", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void playMusic(String filepath){
    	try {
    		File musicPath = new File(filepath);
    		if (musicPath.exists()) {
    			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
    			Clip clip = AudioSystem.getClip();
    			clip.open(audioInput);
    			clip.start();
    		}
    		else {
    			System.out.println("Musique introuvable");
    		}
    		
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainInterface mainInterface = new MainInterface();
                mainInterface.setVisible(true);
            }
        });
    }

}
