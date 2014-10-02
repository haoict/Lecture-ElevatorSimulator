/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.graphics;

import controllers.SimulatorSystem;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.TimerTask;
import models.Controller;
import models.Elevator;

/**
 *
 * @author Huong
 */
public class ElevatorButtonsFrame extends JFrame {
    private Timer timer;
    private int frame_width;
    private int frame_height;
    private ArrayList<ElevatorButtonsPanel> elevatorButtonsPanels;
    private ArrayList<Elevator> elevators;
    
    public ElevatorButtonsFrame (Controller buiding){
        
        this.elevatorButtonsPanels = new ArrayList<ElevatorButtonsPanel>();
        this.elevators = new ArrayList<Elevator>();
        this.elevators = buiding.getElevators();
        //this.setLayout(null);
        this.frame_height = ((buiding.getFloorCount()+1)/2)*35 + 65*3 + 35;
        setVisible(true);
        for(int i = 0; i < this.elevators.size(); i++){
            //this.elevatorButtonsPanels.get(i) = new ElevatorButtonsPanel(null, 11);
            this.elevatorButtonsPanels.add(new ElevatorButtonsPanel(this.elevators.get(i), buiding.getFloorCount()));
            this.elevatorButtonsPanels.get(i).setBounds((i+1)*141, 0, 141, this.frame_height);
            this.add(this.elevatorButtonsPanels.get(i));    
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(true);
        setVisible(true);
        setSize(141*this.elevators.size(), this.frame_height);
        setLocation(141*this.elevators.size(), this.frame_height);
        //timer = new Timer();
        //timer.scheduleAtFixedRate(new ScheduleTask(), 100, 100);
    }

    public ArrayList<ElevatorButtonsPanel> getElevatorButtonsPanels() {
        return elevatorButtonsPanels;
    }

    public void setElevatorButtonsPanels(ArrayList<ElevatorButtonsPanel> elevatorButtonsPanels) {
        this.elevatorButtonsPanels = elevatorButtonsPanels;
    }

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }
//    class ScheduleTask extends TimerTask{
//        public void run() {
//            //elevators.add(null);
//            //System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
//            elevators = SimulatorSystem.getInstance().getController().getElevators();
//            for(int i = 0; i < getElevatorButtonsPanels().size(); i++){
//                getElevatorButtonsPanels().get(i).update(elevators.get(i));
//            }
//            
//        }
//    }
    
}
    



