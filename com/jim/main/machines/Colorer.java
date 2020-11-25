package com.jim.main.machines;
public class Colorer extends Machine {
    public Colorer(int x, int y, int r) {
        super(x, y, r, readImage("color.png"), MachineType.COLORER);
    }
}
