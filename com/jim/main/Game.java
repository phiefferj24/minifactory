package com.jim.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import com.jim.main.machines.*;
import com.jim.main.shapes.Shape;
import com.jim.main.shapes.Shape.ShapeType;

import javax.imageio.ImageIO;
public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    private boolean running = false;
    private Thread thread;
    private final int WORLD_WIDTH = 30;
    private final int WORLD_HEIGHT = 20;
    private final int LOOP_DURATION = 2;
    private final int BUFFER_AMOUNT = 2;
    private final boolean DEBUG = false;
    private int state = 0;
    private boolean moved = false;
    private Machine[][] machines = new Machine[WORLD_HEIGHT][WORLD_WIDTH];
    private BufferedImage gui = readImage("tiles.png");
    public synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
        for(int i = 0; i < 10; i++) {
            machines[i][0] = new Belt(i, 0, 3);
        }
        machines[10][0] = new Belt(10, 0, 0);
        machines[10][1] = new Belt(10, 1, 0);
        machines[10][2] = new BeltCorner(10, 2, 1, 0);
        machines[9][2] = new Belt(9, 2, 1);
        machines[3][0].setShape(new Shape(machines[3][0], ShapeType.CIRCLE, 5));
        machines[2][0].setShape(new Shape(machines[2][0], ShapeType.CIRCLE, 3));
        machines[0][0].setShape(new Shape(machines[0][0], ShapeType.CIRCLE, 1));
        updateNodes();
    }
    public void tick() {
        moveShapes();
    }
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(BUFFER_AMOUNT);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        draw(g);

        g.dispose();
        bs.show();
    }
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        double total = 0;
        int updates = 0;
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                updates++;
                total++;
                state = (int)((total/LOOP_DURATION)%32);
                delta--;
                tick();
            }
            render();
            frames++;
                    
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null) {
                    if(machines[i][j].getMoving()) g.drawImage(Machine.rotate(machines[i][j].getImage().getSubimage(state%8*32, 0, 32, 32), machines[i][j].getRot()),i*32,j*32, null);
                    else g.drawImage(Machine.rotate(machines[i][j].getImage().getSubimage(0, 0, 32, 32), machines[i][j].getRot()),i*32,j*32, null);
                }
            }
        }
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null && machines[i][j].getShape() != null) g.drawImage(machines[i][j].getShape().getImage(), i*32+machines[i][j].getShapeX(), j*32+machines[i][j].getShapeY(), null);
            }
        }
        for(int i = 0; i < 10; i++) {
            if(gui != null) g.drawImage(gui.getSubimage(i*40, 0, 40, 40), this.getWidth()/2-490+i*100, this.getHeight()-100, 80, 80, null);
        }
    }
    public void moveShapes() {
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null) {//ensures that the machine at the specified index exists
                    if(state == 0 && !moved) {//checks if the current state is 0 and that this section hasn't been called yet so it is only called once
                        if(machines[i][j].getNode().getMachine(machines[i][j].getRot()) != null) {//ensures that the machine that it points at exists
                            Machine m = machines[i][j].getNode().getMachine(machines[i][j].getRot());
                            if(m.getRot() != (machines[i][j].getRot()+2)%4) {//ensures that the machine does not face directly into it
                                if(m.getShape() == null && m.getMoving()) {
                                    m.bufferShape(machines[i][j].getShape());
                                    machines[i][j].setShape(null);//sets the next machine's contained shape to the current machine's shape and then removes that shape from itself
                                    machines[i][j].setMoving(true);
                                    if(DEBUG)System.out.println("Moved from "+i+", "+j+" to "+m.getX()+", "+m.getY()+"!");
                                }
                                else if(m.getShape() != null && !m.getMoving() && machines[i][j].getShape() != null) {
                                    machines[i][j].setMoving(false);//stops movement if the next machine has a shape and is stopped
                                    if(DEBUG)System.out.println("Stopped "+i+", "+j+" because shape already there");
                                }
                            }
                            else if(machines[i][j].getShape() != null) machines[i][j].setMoving(false); //if the current conveyor belt has a shape but the machine in front of it is facing the wrong way then stop
                        }
                        else if(machines[i][j].getShape() != null) {
                            machines[i][j].setMoving(false); //if the current conveyor belt has a shape but there is no machine in front of it then pause
                            if(DEBUG)System.out.println("Stopped "+i+", "+j+" because no machine in front");
                        }
                    }
                    if(machines[i][j].getMoving()) {//checks if the machine is currently moving
                        switch(machines[i][j].getRot()) {
                            case 0: machines[i][j].setShapeY(state-16); machines[i][j].setShapeX(0); break;
                            case 1: machines[i][j].setShapeX(-(state-16)); machines[i][j].setShapeY(0); break;
                            case 2: machines[i][j].setShapeY(-(state-16)); machines[i][j].setShapeX(0); break;
                            case 3: machines[i][j].setShapeX(state-16); machines[i][j].setShapeY(0); break;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null) {
                    machines[i][j].loadBuffer(); //loads the currently buffered shape onto the conveyor, outside so multiple movements can't occur at once
                }
            }
        }
        if(state == 0) moved = true;
        else moved = false;
    }
    public void updateNodes() {
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null) {
                    if(i>0)machines[i][j].getNode().setLeft(machines[i-1][j]);
                    if(j>0)machines[i][j].getNode().setTop(machines[i][j-1]);
                    if(i<machines.length-1)machines[i][j].getNode().setRight(machines[i+1][j]);
                    if(j<machines[0].length-1)machines[i][j].getNode().setBot(machines[i][j+1]);
                }
            }
        }
    }
    public static class Coordinate {
        public int x, y;
        public Coordinate(int x, int y) {this.x = x; this.y = y;}
    }
    /*public ArrayList<ArrayList<Coordinate>> generatePathways() {
        ArrayList<ArrayList<Coordinate>> a = new ArrayList<>();
        ArrayList<Coordinate> b = new ArrayList<>();
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines[0].length; j++) {
                if(machines[i][j] != null) {
                    //ensures the machine at the current index exists
                    if(machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4) != null) {
                        //ensures the machine behind it exists
                        Coordinate c = new Coordinate(machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4).getX(), machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4).getY());
                        if(machines[c.y][c.x].getNode().getMachine(machines[c.y][c.x].getRot()) == machines[i][j]) {
                            //ensures the machine behind the current one is facing it
                            if(machines[i][j].getType() == MachineType.BELT) {
                                b.add(c);
                            }
                        }
                    }
                }
            }
        }
        for(Coordinate c : b) {
            for(int i = 0; i < machines.length; i++) {
                for(int j = 0; j < machines[0].length; j++) {

                }
            }
        }
        return a;
    }*///commented out because at least for now it is deprecated
    protected static BufferedImage readImage(String s) {
        try {return ImageIO.read(new File("com"+File.separator+"jim"+File.separator+"assets"+File.separator+"gui"+File.separator+"sheets"+File.separator+s));} 
        catch (IOException e) {System.err.println("Unable to read file "+s);}
        return null;
    }
    public static void main(String[] args) {
        new Window(1920, 1080, "game", new Game());
    }
}

/**
 * Welcome to MiniFactory
 * A game built by Jim Phieffer in Java
 * Inspired by shapez.io, factorio, and satisfactory
 * --PSA--
 * This is a work in progress! 
 * It's not meant to be a full-fledged game, just a learning experience
 * Any suggestions let me know. Especially on how to fix conveyors lol
 * All sprites made in aseprite, I'm not good at it
 */