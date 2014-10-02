/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import controllers.SimulatorSystem;

/**
 *
 * @author Hao
 */
public class ElevatorButton extends Button {
    Elevator belongElevator;
    int numFloor;
    int funNum;
    int numElevator;
    // 0 = Hold; 1 = Close; 2 = Opens
    public ElevatorButton() {
    }

    public ElevatorButton(Elevator el, int numFloor, int funNum) {
        this.belongElevator = el;
        this.numFloor = numFloor;
        this.funNum = funNum;
    }

    public int getNumFloor() {
        return numFloor;
    }

    public void setNumFloor(int numFloor) {
        this.numFloor = numFloor;
    }

    public int getFunNum() {
        return funNum;
    }

    public void setFunNum(int funNum) {
        this.funNum = funNum;
    }
    
    

    public Elevator getBelongElevator() {
        return belongElevator;
    }

    public void setBelongElevator(Elevator belongElevator) {
        this.belongElevator = belongElevator;
    }
    

    @Override
    public void turnOn() {
        // TODO: ngu
        int i = 0;
        for (i = 0; i < SimulatorSystem.getInstance().getController().getElevatorCount(); i++) {
            if (SimulatorSystem.getInstance().getController().getElevators().get(i).equals(getBelongElevator()))
                break;
        }
        SimulatorSystem.getInstance().getElevatorButtonsFrame().getElevatorButtonsPanels().get(i).turnOn(Integer.toString(numFloor));
    }
    
    @Override
    public void turnOff() {
        this.illuminated = false;
    }

    @Override
    protected void pressed() {
        SimulatorSystem.getInstance().getController().getPassengerInform(this);
    }
}
