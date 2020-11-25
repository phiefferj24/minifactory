package com.jim.main.shapes;
import com.jim.main.machines.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
public class Shape {
    public enum ShapeType {
        CIRCLE,
        RECTANGLE,
        DIAMOND
    } 
    private Machine m;
    private BufferedImage img;
    private ShapeType[][] types = new ShapeType[2][2];
    private int[][] colors = new int[2][2];
    private BufferedImage[] images = {readImage("circle.png"), readImage("rectangle.png"), readImage("diamond.png")};
    public Shape(Machine m, ShapeType type, int c) {
        this.m = m;
        switch(type) {
            case CIRCLE: img = readImage("circle.png"); break;
            case RECTANGLE: img = readImage("rectangle.png"); break;
            case DIAMOND: img = readImage("diamond.png");
        }
        types[0][0]=types[0][1]=types[1][0]=types[1][1]=type;
        colors[0][0]=colors[0][1]=colors[1][0]=colors[1][1]=c;
        redrawImage();
    }
    //quadrants - 1=tl; 2=tr; 3=bl; 4=br
    public void setQuadrantType(ShapeType type, int i) {
        switch(i) {
            case 1: types[0][0] = type; break;
            case 2: types[0][1] = type; break;
            case 3: types[1][0] = type; break;
            case 4: types[1][1] = type; break;
            default: break;
        }
        redrawImage();
    }
    //colors - 0=white; 1=red; 2=orange; 3=yellow; 4=green; 5=blue; 6=purple; 7=black
    public void setColor(int c) {
        colors[0][0]=colors[0][1]=colors[1][0]=colors[1][1]=c; 
        redrawImage();
    }
    public void redrawImage() {
        BufferedImage im2 = new BufferedImage(32, 32, img.getType());
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                im2.setRGB(i, j, (types[0][0].equals(ShapeType.CIRCLE)?images[0]:(types[0][0].equals(ShapeType.RECTANGLE)?images[1]:images[2])).getRGB(i+colors[0][0]*32, j));
            }
        }
        for(int i = 16; i < 32; i++) {
            for(int j = 0; j < 16; j++) {
                im2.setRGB(i, j, (types[0][1].equals(ShapeType.CIRCLE)?images[0]:(types[0][1].equals(ShapeType.RECTANGLE)?images[1]:images[2])).getRGB(i+colors[0][1]*32, j));
            }
        }
        for(int i = 0; i < 16; i++) {
            for(int j = 16; j < 32; j++) {
                im2.setRGB(i, j, (types[1][0].equals(ShapeType.CIRCLE)?images[0]:(types[1][0].equals(ShapeType.RECTANGLE)?images[1]:images[2])).getRGB(i+colors[1][0]*32, j));
            }
        }
        for(int i = 16; i < 32; i++) {
            for(int j = 16; j < 32; j++) {
                im2.setRGB(i, j, (types[1][1].equals(ShapeType.CIRCLE)?images[0]:(types[1][1].equals(ShapeType.RECTANGLE)?images[1]:images[2])).getRGB(i+colors[1][1]*32, j));
            }
        }
        img = im2;
    }
    public ShapeType[][] getShapeTypes() {return types;}
    public int[][] getColors() {return colors;}
    public Machine getMachine() {return m;}
    public BufferedImage getImage() {return img;}
    protected static BufferedImage readImage(String s) {
        try {return ImageIO.read(new File("com"+File.separator+"jim"+File.separator+"assets"+File.separator+"shapes"+File.separator+"sheets"+File.separator+s));} 
        catch (IOException e) {System.err.println("Unable to read file "+s);}
        return null;
    }
}
