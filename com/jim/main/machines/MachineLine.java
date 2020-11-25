package com.jim.main.machines;

import com.jim.main.machines.Machine.MachineType;
import java.util.ArrayList;

public class MachineLine {
    private Machine[][] machines;
    private ArrayList<Machine> start = new ArrayList<>();
    public MachineLine(Machine[][] machines) {
        this.machines = machines;
        updateMachineLines(this.machines);
    }
    public void updateMachineLines(Machine[][] machines) {
        for(int i = 0; i < machines.length; i++) {
            for(int j = 0; j < machines.length; j++) {
                if(machines[i][j] != null) {
                    if(machines[i][j].getType() != MachineType.BELTCORNER) {
                        if(machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4) != null && machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4).getRot() != machines[i][j].getRot() || machines[i][j].getNode().getMachine((machines[i][j].getRot()+2)%4) == null) {
                            start.add(machines[i][j]);
                        }
                    }
                    else {
                        if(machines[i][j].getNode().getMachine((machines[i][j].getRot()+machines[i][j].getDir() == 0 ? 1 : 3)%4) != null && machines[i][j].getNode().getMachine((machines[i][j].getRot()+machines[i][j].getDir() == 0 ? 1 : 3)%4).getRot() != machines[i][j].getRot() || machines[i][j].getNode().getMachine((machines[i][j].getRot()+machines[i][j].getDir() == 0 ? 1 : 3)%4) == null) {
                            start.add(machines[i][j]);
                        }
                    }
                }
            }
        }
        
        this.machines = machines;
    }
    public void updateItemPositions() {

    }
}
