package views.graphics;

import java.awt.Color;
import java.awt.Graphics;
import models.Elevator;

/**
 * Class AnimatedElevator 
 *
 * @author Dzung
 */
public class AnimatedElevator extends AnimatedObject {

    public static int ELEVATOR_WIDTH = 250;
    public static int ELEVATOR_HEIGHT = 50;
    public static final int BETWEEN_2_FLOORS_DURATION = 10;
    private final Elevator elevator;
    private int animationStep;

    /**
     * Constructor
     *
     * @param e Elevator
     * @param x placement on x
     * @param y placement on y
     */
    public AnimatedElevator(Elevator e, int x, int y) {
        super(x, y);
        elevator = e;
        speedX = 0;
        speedY = ELEVATOR_HEIGHT / BETWEEN_2_FLOORS_DURATION;
        animationStep = BETWEEN_2_FLOORS_DURATION;
    }

    /**
     * Method that updates the graphics state of the elevator and signals the
     * arrival pattern moving from one floor And passes control to the model to
     * act
     */
    public void updateState() {
        // Animation is coming to a floor, we warn the elevator
        // We reset the elevator animationStep if want to leave
        if (animationStep == BETWEEN_2_FLOORS_DURATION) {
            if (elevator.getMoving()) {
                // The elevator moves still think he is coming,
                // We told him to update his floor
                elevator.setToNextFloor();
            }

            // Is said to act (it may very well decide to leave immediately by placing
            // Variable 'moving' is true).
            elevator.acts();

            // The elevator wants to start afresh
            if (elevator.getMoving()) {
                animationStep = 0;
            }
        }

        // As we are unable has a floor, you move
        if (elevator.getMoving() && (animationStep != BETWEEN_2_FLOORS_DURATION)) {
            animationStep++; // On est entre 2 etages, on se deplace
            y -= elevator.getStep() * speedY;
        }
    }

    /**
     * Method drawing elevator
     *
     * @param g
     */
    public void drawYourself(Graphics g) {
        if (g != null) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, ELEVATOR_WIDTH, ELEVATOR_HEIGHT);
            g.drawString(elevator.getCurrentWeight() + "kg", x + (ELEVATOR_WIDTH / 2) - 30, y + (ELEVATOR_HEIGHT / 2) - 10);
        }
    }
}
