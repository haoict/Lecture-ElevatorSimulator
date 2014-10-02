package models;

import views.graphics.SimulationPanel;
import controllers.SimulatorSystem;
import main.Console;

/**
 *
 * @author x_nem
 */
public class Passenger {

    protected SimulatorSystem controller;

    // personal counter
    private long beginTime;
    private Elevator elevator;
    private int currentFloor;
    private final int wantedFloor;
    private final int mass;

    /**
     * Constructor Passenger with two runs in parameter
     *
     * @param current_floor the floor or the passenger is currently
     * @param wanted_floor upstairs where he wants to go
     * @param mass
     */
    public Passenger(int current_floor, int wanted_floor, int mass) {
        this.controller = SimulatorSystem.getInstance();
        this.currentFloor = current_floor % this.controller.getController().getFloorCount();
        this.wantedFloor = wanted_floor % this.controller.getController().getFloorCount();
        this.elevator = null;
        this.mass = mass;
        resetTime();
    }
    
    public void pressFloorButton() {
        int direction = getWantedFloor() > getCurrentFloor() ? 1:0;
        SimulatorSystem.getInstance().getController().getFloorButtons().get(getCurrentFloor()*2 + direction-1).pressed();
    }
    
    public void pressElevatorButton() {
        for (ElevatorButton eb : SimulatorSystem.getInstance().getController().getElevatorButtons()) {
            if (eb.getBelongElevator() == elevator && eb.getNumFloor() == wantedFloor) {
                eb.pressed();
            }
        }
    }
    
    public void pressWantedFloorButton() {
    }

    public int getTotalMass() {
        return mass;
    }

    public int getPersonCount() {
        return 1;
    }

    public boolean canEnterElevator(Elevator elevator) {
        if (elevator.getCurrentFloor() != this.getCurrentFloor()) return false;
        // If the elevator says it is on alert weight

        if (elevator.isInAlert()) {
            Console.debug("The elevator " + elevator.getIdentifier() + " is alert, I do not ride...");
        } // The asenseur alert is not
        else {
            if (elevator.takePassenger(this)) {
                Console.debug("I get on elevator " + elevator.getIdentifier() + ", I'm going upstairs " + wantedFloor + "! |" + elevator.getPassengerCount() + "|");
                int direction = this.getWantedFloor() > this.getCurrentFloor() ? 1 : 0;
                SimulatorSystem.getInstance().getController().getFloorButtons().get(this.getCurrentFloor() * 2 + direction - 1).turnOff();
            } else {
                Console.debug("I'm too heavy to ride in elevator " + elevator.getIdentifier() + ".");
                //int direction = this.getWantedFloor() > this.getCurrentFloor() ? 1 : 0;
                //MainController.getInstance().getControler().getFloorButtons().get(this.getCurrentFloor() * 2 + direction - 1).turnOn();
            }
        }

        return isInTheElevator();
    }

    public Elevator getElevator() {
        return elevator;
    }

    // Definit dans quel ascenseur l'individu se trouve
    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public boolean isArrived() {
        return (wantedFloor == currentFloor) && !isInTheElevator();
    }

    public boolean isInTheElevator() {
        return elevator != null;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    public long getTime() {
        return ((System.currentTimeMillis() - beginTime) / 1000) * SimulationPanel.framePerSecond;
    }

    public int getWantedFloor() {
        return wantedFloor;
    }

    public boolean isWaitingAtFloor(int floor) {
        return !isArrived() && !isInTheElevator() && currentFloor == floor;
    }

    public boolean isArrivedAtFloor(int floor) {
        return isArrived() && currentFloor == floor;
    }

    public final void resetTime() {
        beginTime = System.currentTimeMillis();
    }

    public void update() {
        if (this.isInTheElevator()) return;
        SimulatorSystem ss = SimulatorSystem.getInstance();
        for (Elevator el : ss.getInstance().getController().getElevators()) {
            if (canEnterElevator(el)) {
                this.setElevator(el);
                pressElevatorButton();
            }
        }
    }
}
