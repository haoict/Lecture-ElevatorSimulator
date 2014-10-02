package views.graphics;

import controllers.SimulatorSystem;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConfigView extends JFrame {

    private static final long serialVersionUID = 8164118974463460991L;
    private static final String POLICE = "Monaco";
    private static final int MIN_FLOOR_COUNT = 1;
    public static final int MAX_FLOOR_COUNT = 10;
    private static final int MIN_ELEVATOR_COUNT = 1;
    public static final int MAX_ELEVATOR_COUNT = 10;
    private static final int MIN_PERSON_COUNT = 0;
    private static final int MAX_PERSON_COUNT = 200;
    //private static final int MIN_GROUP_COUNT = 0;
    //private static final int MAX_GROUP_COUNT = 20;
    private static final int MIN_MASS_PER_ELEVATOR_COUNT = 500;
    public static final int MAX_MASS_PER_ELEVATOR_COUNT = 2000;

    public static final int DEFAULT_FLOOR_COUNT = 10;
    public static final int DEFAULT_ELEVATOR_COUNT = 2;
    public static final int DEFAULT_PERSON_COUNT = 0;
    public static final int DEFAULT_MASS_PER_ELEVATOR_COUNT = 1350;
    // |Nb etage: <slider>
    // |Nb ascenseur: <slider>
    // |Nb individu: <slider>
    // |Nb groupes: <slider>
    //
    private static final Logger logger = Logger.getLogger("ICT03");

    private final JPanel jpanel_principal;
    private final JPanel jpanel_floor_count;
    private final JSlider jslider_floor_count;
    private final JPanel jpanel_elevator_count;
    private final JSlider jslider_elevator_count;
    private final JPanel jpanel_person_per_elevator_count;
    private final JSlider jslider_person_per_elevator_count;
    private final JPanel jpanel_person_count;
    private final JSlider jslider_person_count;
    //private final JPanel jpanel_group_count;
    //private final JSlider jslider_group_count;
    private final JPanel jpanel_start_simulation;
    private final JButton jbutton_start_simulation;

    public ConfigView() throws MalformedURLException {
        logger.setLevel(Level.OFF);

        this.setLocationByPlatform(true);
        this.setSize(600, 400);
        this.setTitle("Simulation of elevator behavior: Java Project 2008");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Container content_pane = this.getContentPane();

        this.jpanel_principal = new JPanel();
        this.jpanel_principal.setLayout(new BoxLayout(jpanel_principal, BoxLayout.PAGE_AXIS));
        {
            // Part of the choice of the number of floors
            this.jpanel_floor_count = new JPanel(new GridLayout(1, 2));
            {
                JLabel choose_floor_count = new JLabel("Number of floors");
                choose_floor_count.setFont(new Font(POLICE, Font.BOLD, 13));
                jpanel_floor_count.add(choose_floor_count);

                JPanel panel = new JPanel();

                JLabel jlabel_floor_count = new JLabel(Integer.toString(DEFAULT_FLOOR_COUNT));
                jlabel_floor_count.setFont(new Font(POLICE, Font.BOLD, 15));

                jslider_floor_count = new JSlider(MIN_FLOOR_COUNT, MAX_FLOOR_COUNT);
                jslider_floor_count.setValue(DEFAULT_FLOOR_COUNT);
                jslider_floor_count.addChangeListener(new SliderUpdateObserver(jlabel_floor_count));

                panel.add(jslider_floor_count);
                panel.add(jlabel_floor_count);
                jpanel_floor_count.add(panel);
            }
            this.jpanel_principal.add(jpanel_floor_count);

            // Part of the choice of the number of lift
            this.jpanel_elevator_count = new JPanel(new GridLayout(1, 2));
            {
                JLabel choose_elevator_count = new JLabel("Number of lifts");
                choose_elevator_count.setFont(new Font(POLICE, Font.BOLD, 13));
                jpanel_elevator_count.add(choose_elevator_count);

                JPanel panel = new JPanel();

                JLabel jlabel_elevator_count = new JLabel(Integer.toString(DEFAULT_ELEVATOR_COUNT));
                jlabel_elevator_count.setFont(new Font(POLICE, Font.BOLD, 15));

                jslider_elevator_count = new JSlider(MIN_ELEVATOR_COUNT, MAX_ELEVATOR_COUNT);
                jslider_elevator_count.setValue(DEFAULT_ELEVATOR_COUNT);
                jslider_elevator_count.addChangeListener(new SliderUpdateObserver(jlabel_elevator_count));

                panel.add(jslider_elevator_count);
                panel.add(jlabel_elevator_count);
                jpanel_elevator_count.add(panel);
            }
            this.jpanel_principal.add(jpanel_elevator_count);

            // Part of the choice of the number of lift
            this.jpanel_person_per_elevator_count = new JPanel(new GridLayout(1, 2));
            {
                JLabel choose_person_per_elevator_count = new JLabel("People max lift");
                choose_person_per_elevator_count.setFont(new Font(POLICE, Font.BOLD, 13));
                jpanel_person_per_elevator_count.add(choose_person_per_elevator_count);

                JPanel panel = new JPanel();
                JLabel jlabel_person_per_elevator_count = new JLabel(Integer.toString(DEFAULT_MASS_PER_ELEVATOR_COUNT));
                jlabel_person_per_elevator_count.setFont(new Font(POLICE, Font.BOLD, 15));

                jslider_person_per_elevator_count = new JSlider(MIN_MASS_PER_ELEVATOR_COUNT, MAX_MASS_PER_ELEVATOR_COUNT);
                jslider_person_per_elevator_count.setValue(DEFAULT_MASS_PER_ELEVATOR_COUNT);
                jslider_person_per_elevator_count.addChangeListener(new SliderUpdateObserver(jlabel_person_per_elevator_count));

                panel.add(jslider_person_per_elevator_count);
                panel.add(jlabel_person_per_elevator_count);
                jpanel_person_per_elevator_count.add(panel);
            }
            this.jpanel_principal.add(jpanel_person_per_elevator_count);

            // Part of the choice of the number of individual
            this.jpanel_person_count = new JPanel(new GridLayout(1, 2));
            {
                JLabel choose_person_count = new JLabel("Number of people (total)");
                choose_person_count.setFont(new Font(POLICE, Font.BOLD, 13));
                jpanel_person_count.add(choose_person_count);

                JPanel panel = new JPanel();

                JLabel jlabel_person_count = new JLabel(Integer.toString((int) DEFAULT_PERSON_COUNT));
                jlabel_person_count.setFont(new Font(POLICE, Font.BOLD, 15));

                jslider_person_count = new JSlider(MIN_PERSON_COUNT, MAX_PERSON_COUNT);
                jslider_person_count.setValue(DEFAULT_PERSON_COUNT);
                jslider_person_count.addChangeListener(new SliderUpdateObserver(jlabel_person_count));

                panel.add(jslider_person_count);
                panel.add(jlabel_person_count);
                jpanel_person_count.add(panel);
            }
            this.jpanel_principal.add(jpanel_person_count);

            // Cadre du bouton "start simulation"
            this.jpanel_start_simulation = new JPanel();
            this.jpanel_start_simulation.setLayout(new BoxLayout(jpanel_start_simulation, BoxLayout.Y_AXIS));
            {
                jbutton_start_simulation = new JButton("Start simulation", new ImageIcon("src/10.png"));
                jbutton_start_simulation.setAlignmentX(CENTER_ALIGNMENT);
                jbutton_start_simulation.setFont(new Font(POLICE, Font.BOLD, 20));
                jbutton_start_simulation.addActionListener(new StartSimulationObserver(this));
                jpanel_start_simulation.add(jbutton_start_simulation);
                jbutton_start_simulation.setEnabled(true);
            }
            this.jpanel_principal.add(jpanel_start_simulation);
        }
        content_pane.add(jpanel_principal);

        this.setResizable(true);
        this.setVisible(true);
    }

    public int get_floor_count() {
        return jslider_floor_count.getValue();
    }

    public int get_elevator_count() {
        return jslider_elevator_count.getValue();
    }

    public int get_person_per_elevator_count() {
        return jslider_person_per_elevator_count.getValue();
    }

    public int get_person_count() {
        return jslider_person_count.getValue();
    }
}

class SliderUpdateObserver implements ChangeListener {

    private final JLabel label;

    public SliderUpdateObserver(JLabel label) {
        this.label = label;
    }

    public void stateChanged(ChangeEvent e) {
        JSlider js = (JSlider) e.getSource();
        this.label.setText(Integer.toString(js.getValue()));
    }
}

class StartSimulationObserver implements ActionListener {

    private final ConfigView window;

    public StartSimulationObserver(ConfigView f) {
        this.window = f;
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public void actionPerformed(ActionEvent e) {
        window.setVisible(false);
        try {
            SimulatorSystem.getInstance().startSimulation(window.get_floor_count(),
                    window.get_elevator_count(), window.get_person_per_elevator_count(), window.get_person_count());
        } catch (InstantiationException ex) {
            Logger.getLogger(StartSimulationObserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StartSimulationObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
