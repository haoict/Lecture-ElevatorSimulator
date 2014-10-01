package strategies.elevators;

import java.util.ArrayList;
import java.util.Random;
import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

public class Nawak extends ElevatorStrategy {

    public Nawak() {
        super();
    }

    public Nawak(Elevator elevator) {
        super(elevator);
    }

    @Override
    public String getName() {
        return "Nawak: Behavior chooses a floor (or there is something to do) randomly.";
    }

    @Override
    public Class getType() {
        return ElevatorStrategy.class;
    }

    @Override
    public void acts() {
        // No call, no nothing, we are at the stop, so good
        if (!elevator.getBuilding().allPassengersAreArrived() && (elevator.getBuilding().getWaitingPersonsCount() != 0 || elevator.getPassengerCount() != 0)) {

            // If you do not yet live floor or that one has arrived to the floor screws
            // We stop and we start work
            if ((elevator.getTargetFloor() == Integer.MAX_VALUE) || (elevator.getCurrentFloor() == elevator.getTargetFloor())) {
                elevator.setMoving(false);

                // Those who want to get off desendent
                elevator.releaseAllArrivedPassengers();

                // If the elevator is not full and that it should not go on ...
                if (!elevator.isFull() && !must_leave_now) {
                    int i = 0;
                    // Long as the lift is not full or warning and we did not test all
                    // Passengers waiting upstairs, one enters the loop
                    while (!elevator.isFull() && !elevator.isInAlert() && i < elevator.getBuilding().getWaitingPersonsCountAtFloor(elevator.getCurrentFloor())) {
                        // We rcupre the ime passenger waiting upstairs for it
                        // Trying to get in (if diffrent null)
                        Passenger p = elevator.getBuilding().getWaitingPassengerAtFloorWithIndex(elevator.getCurrentFloor(), i);
                        if (p != null) {
                            p.canEnterElevator(elevator);
                        }
                        i++;
                    }
                }

                // We rcupre list floors Demands by passengers of the elevator
                ArrayList<Integer> tabWanted = elevator.getFloorsWanted();

                // If the elevator is not full or alert, we merge this list
                // Listing the floors or passengers waiting
                if (!elevator.isFull() && !elevator.isInAlert()) {
                    ArrayList<Integer> tabWaiting = elevator.getBuilding().getFloorWithWaitingPassengers();
                    // Merge des deux listes
                    for (Integer i : tabWaiting) {
                        if (!tabWanted.contains(i)) {
                            tabWanted.add(i);
                        }
                    }
                }

                // It selects a random floor and the elevator targetFloor changing
                int target_floor;
                Random r = new Random();
                if (!tabWanted.isEmpty()) {
                    target_floor = tabWanted.get(r.nextInt(tabWanted.size()));
                    elevator.setTargetFloor(target_floor);
                }
            }

            elevator.incrementStoppedTime(1);
            if ((elevator.getStoppedTime() >= elevator.getStopTime())) {
                elevator.leaveThisFloor();
            }
        } else {
            elevator.setMoving(false);
        }
    }

    @Override
    public boolean takePassenger(Passenger passenger) {
        elevator.incrementStopTime(5 * passenger.getPersonCount());
        return true;
    }

    @Override
    public void releasePassenger(Passenger passenger) {
        elevator.incrementStopTime(5 * passenger.getPersonCount());
        Console.debug("A passenger falls. " + passenger.getCurrentFloor() + " -> " + passenger.getWantedFloor() + " " + passenger.isInTheElevator() + " " + passenger.isArrived());
    }

    @Override
    public void leaveThisFloor() {
    }

}
