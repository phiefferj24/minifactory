package com.jim.main.machines;
public class Split extends Machine {
    public Split(int x, int y, int r) {
        super(x, y, r, readImage("split.png"), MachineType.SPLIT);
    }
}
