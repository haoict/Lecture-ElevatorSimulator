package views.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import models.Elevator;
import controllers.SimulatorSystem;

public class SimulationPanel extends JPanel implements Runnable {

    private static final long serialVersionUID = -478959610785423275L;
    private final ArrayList<AnimatedObject> listAnimatedObjects;
    private final ArrayList<FixedObject> listFixedObjects;
    private final Thread t;
    private final MyFrame frame;
    private boolean inPause = false;
    public static long framePerSecond = 10;

    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public SimulationPanel(MyFrame f, int w, int h) {
        setSize(w, h);
        frame = f;
        listAnimatedObjects = new ArrayList<AnimatedObject>();
        listFixedObjects = new ArrayList<FixedObject>();

        // demarre le thread d'animation
        t = new Thread(this);
        t.start();
    }

    public void addFixedObject(FixedObject o) {
        listFixedObjects.add(o);
    }

    public void addAnimatedObject(AnimatedObject o) {
        listAnimatedObjects.add(o);
    }

    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (!inPause) {
            for (AnimatedObject objetAnime : listAnimatedObjects) {
                // Ici on met toute l'intelligence...
                objetAnime.updateState();
            }
            repaint();
            try {
                Thread.sleep(1000 / framePerSecond); // 10 images/secondes
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (SimulatorSystem.getInstance().allPassengersAreArrived()) {
                //inPause = true;
            }
        }
        displayWaitingTime();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frame.getWidth(), frame.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        if ((g != null) && (getWidth() != 0)) {
            super.paintComponent(g);
            for (FixedObject fixedObject : listFixedObjects) {
                fixedObject.drawYourself(g);
            }
            for (AnimatedObject animatedObject : listAnimatedObjects) {
                animatedObject.drawYourself(g);
            }
        }
    }

    public void pause() {
        System.out.println("Pause!!!!!!!!!!!!!!!!!");
        inPause = !inPause;
        if (!inPause) {
            run();
        }
    }

    public void speedUp() {
        framePerSecond++;
        if (framePerSecond > 50) {
            framePerSecond = 50;
        }
        System.out.println("framePerSecond : " + framePerSecond);
    }

    public void speedDown() {
        framePerSecond--;
        if (framePerSecond <= 0) {
            framePerSecond = (long) 1;
        }
        System.out.println("framePerSecond : " + framePerSecond);
    }

    public void displayWaitingTime() {
        long sum_waiting_times = 0, sum_trip_times = 0;
        for (Elevator elevator : SimulatorSystem.getInstance().getController().getElevators()) {
            System.out.println("Average waiting time (elevator " + elevator.getIdentifier() + ") : " + elevator.getWaitingTime().averageWaitingTime());
            System.out.println("Average travel time (elevator " + elevator.getIdentifier() + ") : " + elevator.getWaitingTime().averageTripTime());
            sum_waiting_times += elevator.getWaitingTime().averageWaitingTime();
            sum_trip_times += elevator.getWaitingTime().averageTripTime();
        }
        System.out.println("Average overall waiting: " + Long.toString((sum_waiting_times / SimulatorSystem.getInstance().getController().getElevatorCount())));
        System.out.println("Average time of global travel : " + Long.toString((sum_trip_times / SimulatorSystem.getInstance().getController().getElevatorCount())));
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void restart() {
        pause();
        frame.setVisible(false);
        try {
            new ConfigView();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
