package models;

import java.util.ArrayList;
import java.util.LinkedList;
import main.Console;
import controllers.MainController;
import java.util.List;
import statistics.Times;
//import strategies.ElevatorStrategy;
import views.graphics.AnimatedElevator;

/**
 *
 * @author x_nem
 * @author remy
 */
public class Elevator {

    private static final int TO_TOP = 1;
    private static final int TO_BOTTOM = -1;    // Pointer to his controller
    private MainController controller;
    private Building building;
    private Times waitingTime;
    private List<Request> requests;

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    public void addRequests(Request request){
        this.requests.add(request);
    }
    private final boolean must_leave_now = false;

    // C'est mieux si l'identifier est unique.
    private int identifier;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    /**
     * Max number of people can physically enter Beyond the lift refuses forcing
     * passengers
     */
    private int maxPersons = 7;

    /**
     * Poids au dela duquel l'ascenseur refuse de bouger, il est physiquement
     * bloqu�.
     */
    private int maxWeight;

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int max_weight) {
        this.maxWeight = max_weight;
    }

    // 
    /**
     * Weight above which, the elevator began to warn that the limit is reached
     * soon What is interesting here is the incorporation of user behavior. In
     * fact, a simple user-mindedness rise even if the elevator says it is on
     * alert.
     */
    private int alertWeight;

    public void setAlertWeight(int alert_weight) {
        this.alertWeight = alert_weight;
    }

    // current Position
    private int currentFloor;

    private int currentWeight = 0;

    // Boolean indicating whether the elevator is moving upwards
    private boolean goingToTop;

    private int stopTime = 0;
    private int stoppedTime = 0;

    private int targetFloor;

    /**
     * This array HAS length equal to the number of floor of the building's
     * elevator Each index Represents a floor (ie: index 0 Represents the
     * ground, and so ...) If a passenger call the elevator from the floor 3,
     * the index value at the 3 increments.
     */
    private LinkedList<Passenger> passengers;
    private boolean moving;
    private AnimatedElevator animatedElevator;

    public Elevator(int max_persons) {
        this.maxPersons = max_persons;
        constructor(max_persons, max_persons - 100);
    }

    /**
     * Constructor method used by Builders Class
     *
     * @Param max_weight Maximum Weight of Elevator
     * @Param alert_weight mass alert the Elevator
     * @Param strategy Strategy Elevator
     */
    private void constructor(int max_weight, int alert_weight) {
        this.controller = MainController.getInstance();
        this.building = controller.getBuilding();
        this.maxWeight = max_weight;
        this.alertWeight = alert_weight;
        this.currentFloor = 0;
        this.goingToTop = true;
        this.passengers = new LinkedList<Passenger>();
        this.requests   = new ArrayList<Request>();
        this.moving = false;
        this.waitingTime = new Times();
        this.targetFloor = Integer.MAX_VALUE;
    }

    // All is done here
    public void acts() {
    //    int direction = this.goingToTop ? 1 : 0;
        // MainController.getInstance().getBuilding().getFloorButtons().get(this.getCurrentFloor() * 2 + direction - 1).turnOff();
        // strategy.acts();
        
        //-------------------------------------------Dung sua o day
      //  Request servingRequest = new Request(this.currentFloor,this.goingToTop);
        for (Request request : this.requests)
            if (request.startFloor == this.currentFloor ){
                this.setMoving(false);
                requests.remove(request);
                int direction = this.goingToTop ? 0 : 1;
                this.goingToTop = request.isDirection();
                MainController.getInstance().getBuilding().getFloorButtons().get(this.getCurrentFloor()*2 + direction-1).turnOff();
                return;
            }
                
        if (this.requests.isEmpty()) {
            this.setMoving(false);
            return;
        }
        this.targetFloor = requests.get(0).startFloor;
        this.goingToTop = this.currentFloor < this.targetFloor;
        this.setMoving(true);
    }

    /**
     * Function returning a boolean for whether the elevator reached the alert
     * threshold weight True if the weight is exceeded warning False if the
     * alert weight is not exceeded
     *
     * @return
     */
    public boolean isInAlert() {
        return currentWeight > alertWeight;
    }

    /**
     * Synchronized function that returns a booléean taking a passenger if the
     * elevator allows returning True if the passenger is back False if the
     * passenger has not been returned
     *
     * @param passenger
     * @return
     */
    public boolean takePassenger(Passenger passenger) {

        if (willBeBlockedWithThisMass(passenger.getTotalMass())) {
            return false;
        } else {
            passengers.add(passenger);
            addToCurrentWeight(passenger.getTotalMass());
            passenger.setElevator(this);
            waitingTime.addWaitingTime(passenger.getTime());
            passenger.resetTime();
            //     strategy.takePassenger(passenger);
            Console.debug("Un passager monte. " + passenger.getCurrentFloor() + " -> " + passenger.getWantedFloor());
            return true;
        }
    }

    public Times getWaitingTime() {
        return waitingTime;
    }

    /**
     * Method that the past passenger in setting out of the elevator
     *
     * @param passenger
     */
    public void releasePassenger(Passenger passenger) {
        if (passengers.contains(passenger)) {
            passengers.remove(passenger);
            removeOfCurrentWeight(passenger.getTotalMass());
            passenger.setElevator(null);
            waitingTime.addTripTime(passenger.getTime());
            //     strategy.releasePassenger(passenger);
        }
    }

    public void releaseAllArrivedPassengers() {
        for (int i = 0; i < getPassengerCount(); i++) {
            if (passengers.get(i).getWantedFloor() == getCurrentFloor()) {
                releasePassenger(passengers.get(i));
            }
        }
    }

    /**
     * Function that returns a boolean True if the elevator is at the top False
     * if the elevator is not
     *
     * @return
     */
    public boolean atTop() {
        return currentFloor >= controller.getBuilding().getFloorCount();
    }

    /**
     * Function that returns a boolean True if the elevator is on the ground
     * floor False if the lift is not in DRC
     *
     * @return
     */
    public boolean atBottom() {
        return currentFloor <= 0;
    }

    /**
     * Return the step (-1 or +1, going for gold to bottom to top) Regarding the
     * variable goingToTop
     *
     * @return -1 or +1
     */
    public int getStep() {
        return goingToTop ? TO_TOP : TO_BOTTOM;
    }

        //// public boolean noCallAtAll() {
    ////		for (int i = 0; i < building.getFloorCount(); i++) {
    ////			if(building.getAskedFloors().get(i) > 0) return false;
    ////		}
    ////		return true;
    //		return building.allPassengersAreArrived();
    //	}
    /**
     * Function that returns a boolean whether the elevator was not called in
     * the same direction
     *
     * @return
     */
    public boolean noCallOnTheWay() {
        if (goingToTop) {
            for (int i = currentFloor; i <= building.getFloorCountWithGround(); i++) {
                if (building.getWaitingPersonsCountAtFloor(i) > 0) {
                    return false;
                }
                for (Passenger p : passengers) {
                    if (p.getWantedFloor() == i) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            for (int i = currentFloor; i >= 0; i--) {
                if (building.getWaitingPersonsCountAtFloor(i) > 0) {
                    return false;
                }
                for (Passenger p : passengers) {
                    if (p.getWantedFloor() == i) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Function that returns a boolean True if the elevator is full False if the
     * elevator is not full
     *
     * @return
     */
    public boolean isFull() {
        return getPassengerCount() >= maxPersons;
    }

    /**
     * Function that returns a boolean True if the elevator has exceeded the
     * maximum weight False if the elevator did not exceed the maximum weight
     *
     * @return
     */
    public boolean isBlocked() {
        return currentWeight >= maxWeight;
    }

    /**
     * Function that returns the number of passenger elevator
     *
     * @return
     */
    public int getPassengerCount() {
        return passengers.size();
    }

    /**
     * Modifying the method variable goingToTop
     *
     * @param goingToTop
     */
    public void setGoingToTop(boolean goingToTop) {
        this.goingToTop = goingToTop;
    }

    /**
     * Function that returns a string list of passengers of the elevator
     *
     * @return
     */
    public LinkedList<Passenger> getPassengers() {
        return passengers;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void changeDirection() {
        goingToTop = !goingToTop;
    }

    public boolean isGoingToTop() {
        return goingToTop;
    }

    public Passenger getLastPassenger() {
        return passengers.get(passengers.size() - 1);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Building getBuilding() {
        return building;
    }

    public void setToNextFloor() {
        currentFloor += getStep();
        for (Passenger p : passengers) {
            p.setCurrentFloor(currentFloor);
        }
    }

    public void setCurrentFloor(int oneFloor) {
        currentFloor = oneFloor;
    }

    public int getMaxPersons() {
        return maxPersons;
    }

    public void addToCurrentWeight(int mass) {
        currentWeight += mass;
    }

    public void removeOfCurrentWeight(int mass) {
        currentWeight -= mass;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean move) {
        moving = move;
    }

    public boolean getMoving() {
        return moving;
    }

    public int getPassengerIndex(Passenger passenger) {
        return passengers.indexOf(passenger);
    }

    public AnimatedElevator getAnimatedElevator() {
        return animatedElevator;
    }

    public void setAnimatedElevator(AnimatedElevator e) {
        animatedElevator = e;
    }

    /**
     * Function returning a Boolean True if the asceseur with the extra weight
     * in parameter exceeds the maximum weight False if the asceseur with the
     * extra weight as a parameter does not exceed the maximum weight
     *
     * @param mass
     * @return
     */
    public boolean willBeBlockedWithThisMass(int mass) {
        return currentWeight + mass >= maxWeight;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getStoppedTime() {
        return stoppedTime;
    }

    public void setStoppedTime(int stoppedTime) {
        this.stoppedTime = stoppedTime;
    }

    public void incrementStopTime(int increment_amount) {
        this.stopTime += increment_amount;
    }

    public void incrementStoppedTime(int increment_amount) {
        this.stoppedTime += increment_amount;
    }

    public void leaveThisFloor() {
        setStopTime(0);
        setStoppedTime(0);
        // The elevator moves
        setMoving(true);
        //   strategy.leaveThisFloor();
    }

    /**
     * Function returning arrayList indices floors where passengers want to go
     *
     * @return
     */
    public ArrayList<Integer> getFloorsWanted() {
        ArrayList<Integer> numberWaiting = new ArrayList<Integer>();
        for (Passenger p : passengers) {
            if (!numberWaiting.contains(p.getWantedFloor())) {
                numberWaiting.add(p.getWantedFloor());
            }
        }
        return numberWaiting;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    /**
     * Sets the stage to which the lift Automatically updates the direction of
     * movement depending on the floor covered
     *
     * @param floor
     */
    public void setTargetFloor(int floor) {
        this.targetFloor = floor;
        if (floor < getCurrentFloor()) {
            setGoingToTop(false);
        } else {
            setGoingToTop(true);
        }
    }

}
