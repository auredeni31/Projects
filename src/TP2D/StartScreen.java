package TP2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StartScreen extends JPanel implements ActionListener, KeyListener {
    private MainInterface mainInterface;

    public StartScreen(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        initializeUI();
        initializeKeyListener();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        JButton startButton = new JButton("Launch Game");
        startButton.addActionListener(this);
        add(startButton, BorderLayout.CENTER);
    }

    private void initializeKeyListener() {
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainInterface.startGame();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            mainInterface.startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
