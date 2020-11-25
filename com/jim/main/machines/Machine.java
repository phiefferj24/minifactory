package com.jim.main.machines;
import com.jim.main.shapes.Shape;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
public class Machine {
    public enum MachineType {
        MERGE,
        SPLIT,
        COLORER,
        BELT,
        BELTCORNER
    }
    protected int rot;
    protected Shape sh;
    protected int x;
    protected int y;
    protected int sx;
    protected int sy;
    protected BufferedImage img;
    protected MachineType type;
    protected boolean moving = true;
    protected Shape bufferedShape;
    protected int dir;
    protected MachineNode node = new MachineNode(null, null, null, null);
    public Machine(int x, int y, int r, BufferedImage im, MachineType t) {
        rot = r;
        this.x = x;
        this.y = y;
        this.sx = 0;
        this.sy = 0;
        img = im;
        type = t;
    }
    public void setShape(Shape shape) {this.sh = shape; sx = x; sy = y;}
    public Shape getShape() {return sh;}
    public void setRot(int r) {rot = r;}
    public int getRot() {return rot;}
    public int getNRot() {return (rot + (dir == 0 ? 3 : 1))%4;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public int getX() {return x;}
    public int getY() {return y;}
    public void setShapeX(int sx) {this.sx = sx;}
    public void setShapeY(int sy) {this.sy = sy;}
    public int getShapeX() {return sx;}
    public int getShapeY() {return sy;}
    public MachineType getType() {return type;}
    public BufferedImage getImage() {return img;}
    public void setMoving(boolean b) {moving = b;}
    public boolean getMoving() {return moving;}
    public void bufferShape(Shape shape) {this.bufferedShape = shape;}
    public Shape getBuffer() {return bufferedShape;}
    public void clearBuffer() {bufferedShape = null;}
    public void loadBuffer() {if(bufferedShape != null){sh = bufferedShape;} bufferedShape = null;}
    public MachineNode getNode() {return node;}
    public void setNode(MachineNode n) {node = n;}
    public int getDir() {return dir;}
    protected void setImage(BufferedImage im2) {img = im2;}
    protected static BufferedImage readImage(String s) {
        try {return ImageIO.read(new File("com"+File.separator+"jim"+File.separator+"assets"+File.separator+"machines"+File.separator+"sheets"+File.separator+s));} 
        catch (IOException e) {System.err.println("Unable to read file "+s);}
        return null;
    }
    public static BufferedImage rotate(BufferedImage image, int r) {
        for(int i = 0; i < r; i++) {
            BufferedImage im2 = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
            for(int j = 0; j < image.getWidth(); j++) {
                for(int k = 0; k < image.getHeight(); k++) {
                    im2.setRGB(image.getHeight()-1-k, j, image.getRGB(j, k));
                }
            }
            image = im2;
        }
        return image;
    }
}
