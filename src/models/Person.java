package models;

import main.Console;
import controllers.MainController;
import javax.swing.JOptionPane;

/**
 * Class Person
 *
 * @author x_nem
 */
public class Person extends Passenger {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private int sex;
    private final int mass;

    /**
     * Constructor Person
     *
     * @param current_floor current floor
     * @param wanted_floor desired floor
     * @param sex sex of the person
     * @param mass mass (weight) of the person
     */
    public Person(int current_floor, int wanted_floor, int sex, int mass) {
        super(current_floor, wanted_floor);
        this.sex = sex;
        this.mass = mass;
    }

    @Override
    public int getTotalMass() {
        return mass;
    }

    @Override
    public int getPersonCount() {
        return 1;
    }

    public int getSex() {
        return sex;
    }

    /**
     * Function returning the sex of the person in string Male or female
     *
     * @param sex
     * @return
     */
    public static String getTextForSex(int sex) {
        switch (sex) {
            case MALE:
                return "male";
            case FEMALE:
                return "female";
        }
        return "unknow";
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * Method implementing the behavior of the person when The elevator in
     * parameter offers to mount
     *
     * @param elevator
     * @return
     */
    @Override
    public boolean canEnterElevator(Elevator elevator) {
        // If the elevator says it is on alert weight

        if (elevator.isInAlert()) {
            Console.debug("The elevator " + elevator.getIdentifier() + " is alert, I do not ride...");
        } // The asenseur alert is not
        else {
            if (elevator.takePassenger(this)) {
                Console.debug("I get on elevator " + elevator.getIdentifier() + ", I'm going upstairs " + wantedFloor + "! |" + elevator.getPassengerCount() + "|");
            } else {
                Console.debug("I'm too heavy to ride in elevator " + elevator.getIdentifier() + ".");
                //int direction = this.getWantedFloor() > this.getCurrentFloor() ? 1 : 0;
                //MainController.getInstance().getBuilding().getFloorButtons().get(this.getCurrentFloor() * 2 + direction - 1).turnOn();
            }
        }

        return isInTheElevator();
    }

}
