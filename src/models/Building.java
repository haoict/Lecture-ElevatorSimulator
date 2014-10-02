package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import main.Console;

/**
 *
 * @author x_nem
 */
public class Building {

    // List lifts the building
    private ArrayList<Elevator> elevators = null;
    // Passenger List of the building in order of arrival
    private LinkedList<Passenger> passengers = null;
    private ArrayList<FloorButton> floorButtons = null;
    private ArrayList<Request> requests = null;
    public void addReqests(Request request){
        
    }
    public ArrayList<FloorButton> getFloorButtons() {
        return floorButtons;
    }

    public void setFloorButtons(ArrayList<FloorButton> floorButtons) {
        this.floorButtons = floorButtons;
    }

    // Number of floor of the building
    private int floorCount;

    /**
     * Constructor Building
     *
     * @param floor_count Levels
     * @param elevators_list
     * @param passengers_list Passenger List
     * @param floorButtons_list
     */
    public Building(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        constructor(floor_count, elevators_list, passengers_list, floorButtons_list);
    }

    /**
     * Constructor Building with a parameter
     *
     * @param floor_count
     */
    public Building(int floor_count) {
        constructor(floor_count, new ArrayList<Elevator>(floor_count), new LinkedList<Passenger>(), new ArrayList<FloorButton>(floor_count*2-2));
    }

    /**
     * Private method used by manufacturers to initialize variables
     *
     * @ Param floor_count Levels
     * @ Param list elevators_list Elevator (Elevator to list)
     * @ Param passengers_list Passengers List
     */
    private void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        this.floorCount = floor_count;
        this.elevators = elevators_list;
        this.passengers = passengers_list;
        this.floorButtons = floorButtons_list;
        this.requests = new ArrayList<Request>();
    }

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }

    public LinkedList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(LinkedList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void addPassengers(Passenger passenger) {
        this.passengers.add(passenger);
    }
    /**
     * Returns the number of floors (excluding DRC)
     *
     * @return
     */
    public int getFloorCount() {
        return floorCount;
    }

    /**
     * Returns the number of floors + the DRC
     *
     * @return
     */
    public int getFloorCountWithGround() {
        return floorCount + 1;
    }

    public int getElevatorCount() {
        return elevators.size();
    }

    /**
     * Retournet the first elevator which is upstairs in parameter If there has
     * not the function returns null
     *
     * @param floor
     * @return
     */
    public Elevator getElevatorAtFloor(int floor) {
        for (Elevator e : elevators) {
            if (e.getCurrentFloor() == floor) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns a boolean as to whether all the passengers arrived at destination
     *
     * @return Returns true if the passengers are all arrived Returns false if
     * all passengers are not all arrived
     *
     */
    public boolean allPassengersAreArrived() {
        for (Passenger passenger : passengers) {
            if (!passenger.isArrived()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of passengers waiting upstairs floor
     *
     * @param floor
     * @return
     */
    public LinkedList<Passenger> getWaitingPassengersAtFloor(int floor) {
        LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
        for (Passenger p : passengers) {
            if (p.isWaitingAtFloor(floor)) {
                ret_list.add(p);
            }
        }
        return ret_list;
    }

    /**
     * Retourne le ieme passagers qui attend à l'étage floor
     *
     * @param floor
     * @param i Index du passager dans la LinkedList de passagers qui attendent
     * à l'étage floor
     * @return
     */
    public Passenger getWaitingPassengerAtFloorWithIndex(int floor, int i) {
        LinkedList<Passenger> ps = getWaitingPassengersAtFloor(floor);
        if (i >= ps.size()) {
            return null;
        } else {
            return ps.get(i);
        }
    }

    /**
     * Returns the passengers waiting th upstairs floor and goes into Executive
     * going_to_top
     *
     * @param floor
     * @param i Index du passager dans la LinkedList de passagers qui attendent
     * à l'étage floor
     * @param going_to_top
     * @return
     */
    public Passenger getWaitingPassengerAtFloorWithIndexInThisDirection(int floor, int i, boolean going_to_top) {
        Passenger p = getWaitingPassengerAtFloorWithIndex(floor, i);

        if (p == null) {
            return null;
        } else {
            if (going_to_top) {
                if (p.getWantedFloor() > p.getCurrentFloor()) {
                    return p;
                }
            } else {
                if (p.getWantedFloor() < p.getCurrentFloor()) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Returns the number of passengers waiting upstairs floor
     *
     * @param floor
     * @return
     */
    public int getWaitingPersonsCountAtFloor(int floor) {
        int sum = 0;
        for (Passenger passenger : getWaitingPassengersAtFloor(floor)) {
            sum += passenger.getPersonCount();
        }
        return sum;
    }

    /**
     * Returns a linked list of passengers
     *
     * @param passenger
     * @return
     */
    public LinkedList<Passenger> getPassengersAtFloor(Passenger passenger) {
        if (passenger.isArrived()) {
            return getArrivedPassengersAtFloor(passenger.getCurrentFloor());
        } else {
            return getWaitingPassengersAtFloor(passenger.getCurrentFloor());
        }
    }

    /**
     * Get the index of a past passenger parameter (useful for drawing the
     * passenger)
     *
     * @param passenger
     * @return
     */
    public int getPassengerIndexAtHisFloor(Passenger passenger) {
        LinkedList<Passenger> list = getPassengersAtFloor(passenger);
        return list.indexOf(passenger);
    }

    /**
     * Returns the number of people waiting for a lift
     *
     * @return
     */
    public int getWaitingPersonsCount() {
        int sum = 0;
        for (Passenger p : passengers) {
            if (!p.isArrived() && !p.isInTheElevator()) {
                sum += p.getPersonCount();
            }
        }
        return sum;
    }

    /**
     * Returns a list Chainé Passenger Connaire for passengers arriving on the
     * floor
     *
     * @param floor
     * @return
     */
    public LinkedList<Passenger> getArrivedPassengersAtFloor(int floor) {
        LinkedList<Passenger> ret_list = new LinkedList<Passenger>();
        for (Passenger p : passengers) {
            if (p.isArrivedAtFloor(floor)) {
                ret_list.add(p);
            }
        }
        return ret_list;
    }

    /**
     * Returns the number of passengers arriving on the floor
     *
     * @param floor
     * @return
     */
    public int getArrivedPassengersCountAtFloor(int floor) {
        int sum = 0;
        for (Passenger passenger : getArrivedPassengersAtFloor(floor)) {
            sum += passenger.getPersonCount();
        }
        return sum;
    }

    /**
     * Returns the floor or there is maximum waiting passengers
     *
     * @return The index for the floor or there is maximum waiting passengers
     */
    public int getMaximumWaitingFloor() {
        int maxCrowdedFloor = 0;
        int numberOfPeople = 0;
        for (int i = 0; i <= floorCount; i++) {
            if (getWaitingPersonsCountAtFloor(i) > numberOfPeople) {
                maxCrowdedFloor = i;
                numberOfPeople = getWaitingPersonsCountAtFloor(i);
            }
        }
        System.out.println("Etage le plus blindé : " + maxCrowdedFloor);
        return maxCrowdedFloor;
    }

    /**
     * Returns indices floors or there are passengers waiting
     *
     * @return Une ArrayList<Integer> the floors and there are passengers
     */
    public ArrayList<Integer> getFloorWithWaitingPassengers() {
        ArrayList<Integer> numberWaiting = new ArrayList<Integer>();
        for (int i = 0; i <= floorCount; i++) {
            if (getWaitingPersonsCountAtFloor(i) > 0) {
                numberWaiting.add(i);
            }
        }
        return numberWaiting;
    }

    void addReqests(int floorNum, boolean direction) {
        this.requests.add(new Request(floorNum, direction));
        this.processNewRequest();
    }
    
    void processNewRequest(){
        Request lastRequest =  this.requests.get(this.requests.size()-1);
        for (Elevator elevator : elevators){
            //List<Request> eleRequests = elevator.getRequests();
            if (elevator.getCurrentFloor() < lastRequest.getStartFloor())
                if (elevator.isGoingToTop()){
                    elevator.addRequests(lastRequest);
                    return;
                }
             if (elevator.getCurrentFloor() > lastRequest.getStartFloor())
                if (!elevator.isGoingToTop()){
                    elevator.addRequests(lastRequest);
                    return;
                }
        }
        Random rand = new Random();
       // Console.debug(elevators.size() +  "  fdd\n");
        elevators.get(rand.nextInt(elevators.size()-1)).addRequests(lastRequest);
            
    }

}
