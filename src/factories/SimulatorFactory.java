package factories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import strategies.ElevatorStrategy;
import models.*;

public class SimulatorFactory {

    public Building getBuilding(int floor_count, ArrayList<Elevator> elevator_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        //		Console.debug("Creating a building with "+floorCount+" floors and "+elevatorList.size()+" lifts.");
        return new Building(floor_count, elevator_list, passengers_list, floorButtons_list);
    }

    public Building getBuilding(int floor_count) {
        return new Building(floor_count);
    }

    public Person getPerson(int max_floor) {
        int sex, mass, current_floor, wanted_floor;
        
        Random rand = new Random();
        sex = rand.nextInt(2);
        mass = 45 + (rand.nextInt(75)); // Entre 45 et 120 kg
        current_floor = rand.nextInt(max_floor);
        
        do {
            wanted_floor = rand.nextInt(max_floor);
        } while (wanted_floor == current_floor);
        
        /*
            String line = JOptionPane.showInputDialog(null, "Enter current_floor:");        
            if (line != null) {
                current_floor = Integer.parseInt(line);
                if (current_floor > 60) {
                    JOptionPane.showMessageDialog(null, "Number must be smaller than 60");
                }
            }
             
            line = JOptionPane.showInputDialog(null, "Enter wanted_floor:");        
            wanted_floor = Integer.parseInt(line);
            if (current_floor > 60) {
                JOptionPane.showMessageDialog(null, "Number must be smaller than 60");
            }
        */      
           
        return new Person(current_floor, wanted_floor, sex, mass);
    }

    public Elevator getElevator(ElevatorStrategy elevatorStrategy, int max_person) {
//		Console.debug("Creation of a lift type "+type);
        return new Elevator(max_person, elevatorStrategy);
    }

}
