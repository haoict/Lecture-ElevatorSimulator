package controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import views.graphics.AnimatedElevator;
import views.graphics.AnimatedPerson;
import views.graphics.FixedFloor;
import views.graphics.MyFrame;
import factories.SimulatorFactory;
import java.util.Random;
import main.Console;
import models.*;
import views.graphics.AnimatedFloorButton;
import views.graphics.UserControlView;

/**
 * SimulatorSystem is built on the model of the Singleton design pattern In fact,
 there can be only one instance of this controller at a time.
 *
 * @ Author Remy
 *
 */
public class SimulatorSystem {

    // The access point a all models (the controler has direct access to the elevators and passengers)
    private static SimulatorSystem INSTANCE = null;
    public static Controller controler = null;
    // Passenger List of the controler in order of arrival
    private LinkedList<Passenger> passengers = null;
    public static MyFrame frame = null;

    public static MyFrame getFrame() {
        return frame;
    }

    public Controller getControler() {
        return controler;
    }

    /**
     * The presence of a private builder removes The public default constructor.
     */
    private SimulatorSystem() {
    }

    /**
     * The synchronized keyword on the method for creating Prevents multiple
     * instantiation even Different threads.
     *
     * @return The single instance of the singleton.
     */
    public synchronized static SimulatorSystem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulatorSystem();
        }
        return INSTANCE;
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void startSimulation(int floor_count, int elevator_count, int person_per_elevator, int person_count) throws InstantiationException, IllegalAccessException {

        Console.info("Launching a game with " + floor_count + " floors, " + elevator_count
                + " lifts, " + person_per_elevator + " max people on lift, " + person_count + ".");

        SimulatorFactory sf = new SimulatorFactory();

        // Constructs the controlers.
        controler = sf.getControler(floor_count);

        // Constructs the elevators
        ArrayList<Elevator> elevators = new ArrayList<Elevator>(elevator_count);
        Elevator elevator;
        for (int i = 1; i <= elevator_count; i++) {
            elevator = sf.getElevator(person_per_elevator);
            elevator.setIdentifier(i);
            //Placement des Elevators
            Random rand = new Random();
            elevator.setCurrentFloor(rand.nextInt(floor_count + 1));
            elevators.add(elevator);
        }
        // Add elevators to the controler
        controler.setElevators(elevators);

        // Graphics!
        frame = new MyFrame(elevator_count, controler.getFloorCount());
        //ElevatorButtonsFrame bframe = new ElevatorButtonsFrame(elevator_count, controler.getFloorCount());
        new UserControlView();

        ArrayList<FloorButton> fBs = new ArrayList<FloorButton>();
        for (int i = 0; i < controler.getFloorCount(); i++) {
            frame.addFixedObject(new FixedFloor(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), i));

            if (i != 0) {
                FloorButton fb = new FloorButton(i, false);
                fBs.add(fb);
                frame.addAnimatedObject(new AnimatedFloorButton(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), fb));
            }

            if (i != controler.getFloorCount() - 1) {
                FloorButton fb = new FloorButton(i, true);
                fBs.add(fb);
                frame.addAnimatedObject(new AnimatedFloorButton(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)) - 10, fb));
            }
            controler.setFloorButtons(fBs);
            frame.addFixedObject(new FixedFloor(FixedFloor.FLOOR_WIDTH + (elevator_count * AnimatedElevator.ELEVATOR_WIDTH), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), i));
        }

        for (int i = 0; i < elevators.size(); i++) {
            AnimatedElevator e = new AnimatedElevator(elevators.get(i), FixedFloor.FLOOR_WIDTH + (AnimatedElevator.ELEVATOR_WIDTH * i), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (elevators.get(i).getCurrentFloor() + 1)));
            frame.addAnimatedObject(e);
            elevators.get(i).setAnimatedElevator(e);
        }

        // Constructs the passengers (only persons for now)
        passengers = new LinkedList<Passenger>();
        int j = 0;
        while (j < person_count) {
            passengers.add(sf.getRandomPerson(controler.getFloorCount()));
            j++;
        }

        Passenger passenger;
        for (int i = 0; i < getPassengers().size(); i++) {
            passenger = getPassengers().get(i);
            frame.addAnimatedObject(new AnimatedPerson((Passenger) passenger, 
                    FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * getPassengerIndexAtHisFloor(passenger)), 
                    MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
        }
    }

    
    // Passengers control
    public LinkedList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(LinkedList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void addPassengers(Passenger passenger) {
        this.passengers.add(passenger);
        frame.addAnimatedObject(new AnimatedPerson((Passenger) passenger, 
                    FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * getPassengerIndexAtHisFloor(passenger)), 
                    MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
    }
    
     /**
     * Returns a boolean as to whether all the passengers arrived at destination
     *
     * @return Returns true if the passengers are all arrived Returns false if
     * all passengers are not all arrived
     *
     */
    public boolean allPassengersAreArrived() {
        if (passengers == null) return false;
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
     * Returns the ith passengers waiting upstairs floor
     *
     * @param floor
     * @param i Index passenger in the LinkedList of passengers waiting upstairs floor
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
     * @param i Index passenger in the LinkedList of passengers waiting upstairs floor
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
     * Returns a list Chainé Passenger Connaire for passengers arriving on the floor
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
        for (int i = 0; i <= controler.getFloorCount(); i++) {
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
        for (int i = 0; i <= controler.getFloorCount(); i++) {
            if (getWaitingPersonsCountAtFloor(i) > 0) {
                numberWaiting.add(i);
            }
        }
        return numberWaiting;
    }

    
}
