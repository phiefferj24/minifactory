package com.jim.main.machines;
public class Merge extends Machine {
    public Merge(int x, int y, int r) {
        super(x, y, r, readImage("merge.png"), MachineType.MERGE);
    }
}
