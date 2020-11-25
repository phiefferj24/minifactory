package com.jim.main.machines;
public class Belt extends Machine {
    public Belt(int x, int y, int r) {
        super(x, y, r, readImage("belt.png"), MachineType.BELT);
    }
}
