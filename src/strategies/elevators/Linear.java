package strategies.elevators;

import controllers.MainController;
import main.Console;
import models.Elevator;
import models.Passenger;
import strategies.ElevatorStrategy;

/**
 *
 * @author francois
 *
 */
public class Linear extends ElevatorStrategy {

    public Linear() {
        super();
    }

    public Linear(Elevator elevator) {
        super(elevator);
    }

    @Override
    public String getName() {
        return "Linear: linear behavior without optimization durations waiting.";
    }

    @Override
    public Class getType() {
        return ElevatorStrategy.class;
    }

    @Override
    public void acts() {
        // No call, no nothing, we are at the stop, so good
        if (!elevator.getBuilding().allPassengersAreArrived() && (elevator.getBuilding().getWaitingPersonsCount() != 0 || elevator.getPassengerCount() != 0)) {
            elevator.setMoving(false);

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

            elevator.incrementStoppedTime(1);

            if ((elevator.isGoingToTop() && elevator.atTop()) || (!elevator.isGoingToTop() && elevator.atBottom()) || (elevator.noCallOnTheWay())) {
                elevator.changeDirection(); // Change of direction for the next move
            }

            if (elevator.getStoppedTime() >= elevator.getStopTime()) {
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
