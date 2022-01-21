package com.example.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 500;
    static final int SCREEN_HEIGHT = 500;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;
    static final int GAME_UNITS = SCREEN_HEIGHT*SCREEN_WIDTH;
    final int[] snakeX = new int[GAME_UNITS];
    final int[] snakeY = new int[GAME_UNITS];
    int appleX;
    int appleY;
    int applesEaten = 0;
    int bodyLen = 5;
    char direction = 'R';
    boolean running = false;
    boolean pause = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        placeApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawGame(g);
    }
    public void drawGame(Graphics g){
        if(running){
/*        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(0,i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            g.drawLine(i * UNIT_SIZE,0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }*/

            g.setColor(Color.green);
            for (int i = 0; i < bodyLen; i++) {
                if(i == 0){
                    //g.setColor(Color.getHSBColor(random.nextFloat(),80, 10));
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }else{
                    g.setColor(Color.GREEN);
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.getHSBColor(50,50,50));
            g.setFont(new Font("Baskerville", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score " + applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score " + applesEaten))/2, g.getFont().getSize() );

            g.setColor(Color.getHSBColor(random.nextFloat(),80, 10));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        }
        else{
            gameOver(g);
        }
    }
    public void move(){
        for (int i = bodyLen; i > 0; i--) {
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }

        switch (direction) {
            case 'R' -> snakeX[0] = snakeX[0] + UNIT_SIZE;
            case 'L' -> snakeX[0] = snakeX[0] - UNIT_SIZE;
            case 'U' -> snakeY[0] = snakeY[0] - UNIT_SIZE;
            case 'D' -> snakeY[0] = snakeY[0] + UNIT_SIZE;
        }
    }
    public void placeApple(){
        appleX = random.nextInt(0,SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(0,SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
        //System.out.println(appleX);
        //System.out.println(appleY);
    }
    public void checkApple(){
        if(snakeX[0] == appleX && snakeY[0] == appleY){
            bodyLen++;
            applesEaten++;
            placeApple();
        }
    }
    public void checkCollision(){
        boolean hitWall = false;
        for (int i = bodyLen; i > 0; i--) {
            if (snakeX[i] == snakeX[0] && snakeY[i] == snakeY[0]) {
                running = false;
                break;
            }
        }
        if(snakeX[0] < 0 ){
            snakeX[0] = 0;
            hitWall = true;
            //running = false;
        }
        if(snakeX[0] > SCREEN_WIDTH){
            snakeX[0] = SCREEN_WIDTH;
            hitWall = true;
            //running = false;
        }
        if(snakeY[0] > SCREEN_HEIGHT){
            snakeY[0] = SCREEN_HEIGHT;
            hitWall = true;
            //running = false;
        }
        if(snakeY[0] < 0){
            snakeY[0] = 0;
            hitWall = true;
            //snakeY[0] = SCREEN_HEIGHT;
            //running = false;
        }

        if(hitWall){
            bodyLen--;
            applesEaten--;
        }
        if(applesEaten < 0){
            running = false;
            applesEaten = 0;
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.GREEN);
        g.setFont(new Font("Baskerville", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH-metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2 );
        timer.stop();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            //System.out.println(e.getKeyCode());
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP :
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
            }
            //System.out.println(direction);
        }
    }
}
