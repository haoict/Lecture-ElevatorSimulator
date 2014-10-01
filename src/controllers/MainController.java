package controllers;

import java.util.ArrayList;
import java.util.LinkedList;

import strategies.ElevatorStrategy;
import views.graphics.AnimatedElevator;
import views.graphics.AnimatedPerson;
import views.graphics.FixedFloor;
import views.graphics.MyFrame;
import factories.SimulatorFactory;
import java.util.Random;
import main.Console;
import models.*;
import views.graphics.AnimatedFloorButton;
import views.graphics.ElevatorButtonsFrame;
import views.graphics.UserControlView;

/**
 * MainController is built on the model of the Singleton design pattern In fact,
 * there can be only one instance of this controller at a time.
 *
 * @ Author Remy
 *
 */
public class MainController {

    // The access point a all models (the building has direct access to the elevators and passengers)
    private static MainController INSTANCE = null;
    public static Building building = null;
    public static MyFrame frame = null;

    public static MyFrame getFrame() {
        return frame;
    }

    public Building getBuilding() {
        return building;
    }

    /**
     * The presence of a private builder removes The public default constructor.
     */
    private MainController() {
    }

    /**
     * The synchronized keyword on the method for creating Prevents multiple
     * instantiation even Different threads.
     *
     * @return The single instance of the singleton.
     */
    public synchronized static MainController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainController();
        }
        return INSTANCE;
    }

    public void startSimulation(int floor_count, int elevator_count, int person_per_elevator, int person_count, ElevatorStrategy elevator_strategy) throws InstantiationException, IllegalAccessException {

        Console.info("Launching a game with " + floor_count + " floors, " + elevator_count
                + " lifts, " + person_per_elevator + " max people on lift, " + person_count + ".");

        SimulatorFactory sf = new SimulatorFactory();

        // Constructs the buildings.
        building = sf.getBuilding(floor_count);

        // Constructs the elevators
        ArrayList<Elevator> elevators = new ArrayList<Elevator>(elevator_count);
        Elevator elevator;
        for (int i = 1; i <= elevator_count; i++) {
            // Avec plugin
            elevator = sf.getElevator((ElevatorStrategy) elevator_strategy.getClass().newInstance(), person_per_elevator);
            elevator.setIdentifier(i);
            //Placement des Elevators
            Random rand = new Random();
            elevator.setCurrentFloor(rand.nextInt(floor_count + 1));
            elevators.add(elevator);
        }
        // Add elevators to the building
        building.setElevators(elevators);

        // Graphics!
        frame = new MyFrame(elevator_count, building.getFloorCountWithGround());
        ElevatorButtonsFrame bframe = new ElevatorButtonsFrame(elevator_count, building.getFloorCountWithGround());
        UserControlView userControlView = new UserControlView();

        ArrayList<FloorButton> fBs = new ArrayList<FloorButton>();
        for (int i = 0; i < building.getFloorCountWithGround(); i++) {
            frame.addFixedObject(new FixedFloor(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), i));

            if (i != 0) {
                FloorButton fb = new FloorButton(i, true);
                fBs.add(fb);
                frame.addAnimatedObject(new AnimatedFloorButton(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), fb));
            }

            if (i != building.getFloorCountWithGround() - 1) {
                FloorButton fb = new FloorButton(i, false);
                fBs.add(fb);
                frame.addAnimatedObject(new AnimatedFloorButton(0, MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)) - 10, fb));
            }
            building.setFloorButtons(fBs);
            frame.addFixedObject(new FixedFloor(FixedFloor.FLOOR_WIDTH + (elevator_count * AnimatedElevator.ELEVATOR_WIDTH), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (i + 1)), i));
        }

        for (int i = 0; i < elevators.size(); i++) {
            AnimatedElevator e = new AnimatedElevator(elevators.get(i), FixedFloor.FLOOR_WIDTH + (AnimatedElevator.ELEVATOR_WIDTH * i), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * (elevators.get(i).getCurrentFloor() + 1)));
            frame.addAnimatedObject(e);
            elevators.get(i).setAnimatedElevator(e);
        }

        // Constructs the passengers (only persons for now)
        LinkedList<Passenger> passengers = new LinkedList<Passenger>();
        int j = 0;
        while (j < person_count) {
            passengers.add(sf.getPerson(building.getFloorCountWithGround()));
            j++;
        }
        // Add passengers to the building
        building.setPassengers(passengers);

        Passenger passenger;
        for (int i = 0; i < building.getPassengers().size(); i++) {
            passenger = building.getPassengers().get(i);
            if (passenger instanceof Person) {
                frame.addAnimatedObject(new AnimatedPerson((Person) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
            }
        }
    }

}
