/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views.graphics;

import static java.awt.Component.CENTER_ALIGNMENT;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import models.Person;
import controllers.MainController;
import static controllers.MainController.building;
import javax.swing.JOptionPane;

/**
 *
 * @author Hao
 */
public class UserControlView extends JFrame {
    
    
    private final JPanel jpanel_principal;
    private final JLabel lblSomeText;
    private final JTextPane ltpWanted;
    private final JLabel lblSomeText2;
    private final JTextPane ltpCurrent;
    
    public UserControlView() {
        this.setLocationByPlatform(true);
        this.setSize(200, 200);
        this.setTitle("User control");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.jpanel_principal = new JPanel();
        this.jpanel_principal.setLayout(new BoxLayout(jpanel_principal, BoxLayout.Y_AXIS));
        
        lblSomeText = new JLabel("Current Floor");
        jpanel_principal.add(lblSomeText);
        
        ltpWanted = new JTextPane();
        jpanel_principal.add(ltpWanted);
        
        lblSomeText2 = new JLabel("Wanted Floor");
        jpanel_principal.add(lblSomeText2);
        
        ltpCurrent = new JTextPane();
        jpanel_principal.add(ltpCurrent);
        
        JButton jbutton_start_simulation = new JButton("Start simulation", new ImageIcon("src/10.png"));
                jbutton_start_simulation.setAlignmentX(CENTER_ALIGNMENT);
                jbutton_start_simulation.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonAddPersionActionPerformed(evt);
                    }
                });
                jpanel_principal.add(jbutton_start_simulation);
                
        this.add(jpanel_principal);

        this.setVisible(true);
    }
    
    private void jButtonAddPersionActionPerformed(java.awt.event.ActionEvent evt) {         
        Random rand = new Random();
        int sex = rand.nextInt(2);
        int mass = 45 + (rand.nextInt(75)); // Entre 45 et 120 kg
        Person passenger = new Person(Integer.parseInt(ltpWanted.getText()), Integer.parseInt(ltpCurrent.getText()), sex, mass);
 
        MainController.getInstance().getFrame().addAnimatedObject(new AnimatedPerson((Person) passenger, FixedFloor.FLOOR_WIDTH - AnimatedPerson.PERSON_WIDTH - (AnimatedPerson.PERSON_WIDTH * building.getPassengerIndexAtHisFloor(passenger)), MyFrame.frame_height - (AnimatedElevator.ELEVATOR_HEIGHT * passenger.getCurrentFloor()) - AnimatedPerson.PERSON_HEIGHT));
        MainController.getInstance().getBuilding().addPassengers(passenger);
        
        int direction = passenger.getWantedFloor() > passenger.getCurrentFloor() ? 1:0;
        
        MainController.getInstance().getBuilding().getFloorButtons().get(passenger.getCurrentFloor()*2 + direction-1).turnOn();
    }          
}
