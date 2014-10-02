/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package views.graphics;

import static java.awt.Component.CENTER_ALIGNMENT;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import controllers.SimulatorSystem;
import static controllers.SimulatorSystem.frame;
import models.Passenger;

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
        this.setSize(200, 150);
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
        
        JButton jbutton_start_simulation = new JButton("Add passenger");
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
        
        int mass = 45 + (rand.nextInt(75)); // Entre 45 et 120 kg
        try {
            if (Integer.parseInt(ltpWanted.getText()) > SimulatorSystem.getInstance().getControler().getFloorCount() ||
                Integer.parseInt(ltpWanted.getText()) < 0 ||
                Integer.parseInt(ltpCurrent.getText()) > SimulatorSystem.getInstance().getControler().getFloorCount() ||
                Integer.parseInt(ltpCurrent.getText()) < 0)
            {
                System.out.printf("Current floor and wanted floor must be between 0 and %d\n", SimulatorSystem.getInstance().getControler().getFloorCount());
                return;
            }
            
            Passenger passenger = new Passenger(Integer.parseInt(ltpWanted.getText()), Integer.parseInt(ltpCurrent.getText()), mass);
            passenger.pressFloorButton();
            
            SimulatorSystem ss = SimulatorSystem.getInstance();
            
            ss.getInstance().addPassengers(passenger);

            //int direction = passenger.getWantedFloor() > passenger.getCurrentFloor() ? 1:0;
            //MainController.getInstance().getControler().getFloorButtons().get(passenger.getCurrentFloor()*2 + direction-1).turnOn();
        }
        catch (NumberFormatException e) {
            System.out.println("Current floor and wanted floor must be a number");
        }    
    }          
}
