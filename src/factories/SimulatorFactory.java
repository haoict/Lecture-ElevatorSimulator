package factories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
//import strategies.ElevatorStrategy;
import models.*;

public class SimulatorFactory {

    public Controller getControler(int floor_count, ArrayList<Elevator> elevator_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        //		Console.debug("Creating a controler with "+floorCount+" floors and "+elevatorList.size()+" lifts.");
        return new Controller(floor_count, elevator_list, passengers_list, floorButtons_list);
    }

    public Controller getControler(int floor_count) {
        return new Controller(floor_count);
    }

    public Passenger getRandomPerson(int max_floor) {
        int mass, current_floor, wanted_floor;
        
        Random rand = new Random();
        mass = 45 + (rand.nextInt(75)); // Entre 45 et 120 kg
        current_floor = rand.nextInt(max_floor);
        
        do {
            wanted_floor = rand.nextInt(max_floor);
        } while (wanted_floor == current_floor);   
           
        return new Passenger(current_floor, wanted_floor, mass);
    }

    public Elevator getElevator(int max_person){
        return new Elevator(max_person);
    }

}
