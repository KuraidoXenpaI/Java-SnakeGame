import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FontFormatException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

class GamePanel extends JPanel implements ActionListener {

    private int HEIGHT = 800; // HEIGHT value of the JPanel
    private int WIDTH = 800; // WIDTH value of the JPanel
    private int ITEM_SIZE = 25; // unit size for every item
    private int DELAY = 80; // Speed of the game

    // Sets the overall unit for every item to be placed in the panel's grid
    private int UNITS = (int) ((HEIGHT*WIDTH)/Math.pow(ITEM_SIZE, 2));

    // Properties for the snake
    private int snakeLength = 6;
    private int[] snakeXAxis = new int[UNITS]; // x coordinates for the snake
    private int[] snakeYAxis = new int[UNITS]; // y coordinates for the snake
    private char snakeDirection = 'R';

    private Timer t;

    private boolean running = false;

    private int score;

    // Declare fonts to be used in the game
    private Font papyrus_75;
    private Font papyrus_35;
    private Font comic_sans;
    private Font snake_chan;

    // apple locations
    private int AppleXAxis;
    private int AppleYAxis;

    private Random r = new Random();

    GamePanel() {
        super(); // add super() call to constructor

        // import papyrus font size 75
        try {
            this.papyrus_75 = Font.createFont(Font.TRUETYPE_FONT, new File("source/PAPYRUS.ttf")).deriveFont(75f);
        } catch (IOException| FontFormatException e) {
            e.printStackTrace();
        }

        // import papyrus font size 35
        try {
            this.papyrus_35 = Font.createFont(Font.TRUETYPE_FONT, new File("source/PAPYRUS.ttf")).deriveFont(35f);
        } catch (IOException| FontFormatException e) {
            e.printStackTrace();
        }

        // import snake chan font
        try {
            this.snake_chan = Font.createFont(Font.TRUETYPE_FONT, new File("source/Snake Chan.ttf")).deriveFont(35f);
        } catch (IOException| FontFormatException e) {
            e.printStackTrace();
        }

        // import ComicSansMS
        try {
            this.comic_sans = Font.createFont(Font.TRUETYPE_FONT, new File("source/ComicSansMS3.ttf")).deriveFont(15f);
        } catch (IOException| FontFormatException e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(HEIGHT, WIDTH));
        this.setBackground(new Color(20, 40, 20));
        this.setFocusable(true);
        this.addKeyListener(new KeyBindings());
        startGame();
    }

    public void startGame() {
        running = true;
        t = new Timer(DELAY, this);
        t.start();

        // To draw the apple on the screen, AppleXAxis and AppleYAxis need to be set to a valid coordinate
        // We call spawnApple() to set these values
        spawnApple();
    }

    // Pass all the draw functions in the painComponent
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {     
        // If the game is running spawn put apples on the screen and make a snake 
        if (running) {
            g.setColor(new Color(180, 50, 0));
            g.fillOval(AppleXAxis, AppleYAxis, ITEM_SIZE, ITEM_SIZE);

            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.gray);
                    g.fillRect(snakeXAxis[i], snakeYAxis[i], ITEM_SIZE, ITEM_SIZE);
                } else {
                    g.setColor(new Color(0, 120, 50));
                    g.fillRect(snakeXAxis[i], snakeYAxis[i], ITEM_SIZE, ITEM_SIZE);
                }
            }

            // Shows the score on the screen
            g.setColor(new Color(250, 250, 250, 150));
            g.setFont(snake_chan);

            g.drawString("Score: " + score, 300, 45);

        } else {
            gameOver(g);
        }
    }

    public void spawnApple() {
        AppleXAxis = r.nextInt((int)(WIDTH/ITEM_SIZE))*ITEM_SIZE; // multiply by ITEM_SIZE
        AppleYAxis = r.nextInt((int)(HEIGHT/ITEM_SIZE))*ITEM_SIZE; // multiply by ITEM_SIZE
    }

    public void moveSnake() {
        for (int i = snakeLength; i > 0; i--) {
            snakeXAxis[i] = snakeXAxis[i-1];
            snakeYAxis[i] = snakeYAxis[i-1];
        }

        // set a direction property for the snake's movement
        switch (snakeDirection) {
            case 'U':
                snakeYAxis[0] = snakeYAxis[0] - ITEM_SIZE;
                break;

            case 'D':
                snakeYAxis[0] = snakeYAxis[0] + ITEM_SIZE;
                break;

            case 'R':
                snakeXAxis[0] = snakeXAxis[0] + ITEM_SIZE;
                break;
        
            case 'L':
                snakeXAxis[0] = snakeXAxis[0] - ITEM_SIZE;
                break;
        }

    }

    public void checkCollision() {
        // Check for body collisions
        for (int i = snakeLength; i > 0 ; i--) {
            if ((snakeXAxis[0]==snakeXAxis[i])&&(snakeYAxis[0]==snakeYAxis[i])){
                running = false;
            }
        }

        // Check for right and bottom wall collision
        if ((snakeXAxis[0]>WIDTH-25)||(snakeYAxis[0]>HEIGHT-25)) {
            running = false;
        }

        // Check for left and upper wall collision
        if ((snakeXAxis[0]<0)||(snakeYAxis[0]<0)) {
            running = false;
        }

        if (running != true) {
            t.stop();
        }

    }

    public void checkAppleCollision() {
        if ((snakeXAxis[0] == AppleXAxis)&&(snakeYAxis[0] == AppleYAxis)) {
            snakeLength++;
            score++;
            spawnApple();
        }
    }

    public void gameOver(Graphics g) {
        // Show Game Over Text
        g.setColor(Color.red);
        g.setFont(papyrus_75);

        g.drawString("GAME OVER", 95, HEIGHT/2);

        // Show score text below the Game Over text
        g.setColor(Color.white);
        g.setFont(papyrus_35);
        g.drawString("Score: " + score, 325, (HEIGHT/2)+50);

        // Add a label for game restart
        g.setColor(Color.gray);
        g.setFont(comic_sans);
        g.drawString("Press SPACE to restart", 310, (HEIGHT/2)+75);

    }

    // Resets all the properties of the snake
    public void restart() {
        // Properties for the snake
        snakeLength = 6;
        snakeXAxis = new int[UNITS]; // x coordinates for the snake
        snakeYAxis = new int[UNITS]; // y coordinates for the snake
        snakeDirection = 'R';
        score = 0;

        // Sets the running boolean variable back to true and start game
        running = true;
        startGame();
    }

    // Method for game functioning
    public void actionPerformed(ActionEvent e) {
        // if the game is running perform these methods for functionality
        if (running) {            
            moveSnake();
            checkAppleCollision();
            checkCollision();
        }
        
        repaint();
    }

    // Method for key strokes
    class KeyBindings extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snakeDirection != 'R') {
                        snakeDirection = 'L';
                    }
                    break;
            
                case KeyEvent.VK_RIGHT:
                    if (snakeDirection != 'L') {
                        snakeDirection = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snakeDirection != 'D') {
                        snakeDirection = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snakeDirection != 'U') {
                        snakeDirection = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        restart();
                    }
                    repaint();
                    break;

            }
        }
    }

}
