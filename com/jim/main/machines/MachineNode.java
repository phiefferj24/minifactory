package com.jim.main.machines;

public class MachineNode {
    private Machine t, b, l, r;
    public MachineNode(Machine t, Machine b, Machine l, Machine r) {
        this.t = t;
        this.b = b;
        this.l = l;
        this.r = r;
    }
    public Machine getTop() {return t;}
    public Machine getBot() {return b;}
    public Machine getLeft() {return l;}
    public Machine getRight() {return r;}
    public void setTop(Machine t) {this.t = t;}
    public void setBot(Machine b) {this.b = b;}
    public void setLeft(Machine l) {this.l = l;}
    public void setRight(Machine r) {this.r = r;}
    public Machine getMachine(int x) {
        switch(x) {
            case 0: return b;
            case 1: return l;
            case 2: return t;
            case 3: return r;
            default: return null;
        }
    }
}
