package com.jim.main.machines;
public class BeltCorner extends Machine {
    public BeltCorner(int x, int y, int r, int dir) {
        super(x, y, r, readImage(dir == 0 ? "belt-corner.png" : "belt-corner-flip.png"), MachineType.BELTCORNER);
        super.dir = dir;
    }
}
