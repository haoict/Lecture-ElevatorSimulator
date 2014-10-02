/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import models.Elevator;

/**
 *
 * @author Admin
 */
public class ElevatorButtonsPanel extends JPanel implements ActionListener{

	//private static final long serialVersionUID = -8699676873901285644L;
	public static int frame_width;
	public static int frame_height;
	private JPanel mainPanel;
        private JPanel panel1;
        private JPanel panel2;
        private ArrayList<JButton> buttons;
        private JButton buttonHold, buttonOpen, buttonClose;
        private Elevator elevator;
        private int numFloor;
	
    public ElevatorButtonsPanel(Elevator elevator, int numFloor) {
    	frame_width = 300;
    	frame_height = 300;
        this.numFloor = numFloor;
        this.elevator = elevator;
//    	System.out.println(frame_width+"x"+frame_height);
        //this.getContentPane().setLayout(new FlowLayout());
        setSize(frame_width, frame_height);
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //panel = new SimulationPanel(this, getWidth(), getHeight());
        //mainPanel = new JPanel();
        //mainPanel.setLayout(new FlowLayout());
        buttons = new ArrayList<JButton>();
        this.setLayout(null);
        for(int i = 1; i <= numFloor; i++){
            buttons.add(new JButton(Integer.toString(i)));
            buttons.get(i-1).setBounds(((i-1)%2)*65, (35*((i+1)/2))-35, 60, 30);
            buttons.get(i-1).setActionCommand(Integer.toString(i));
            buttons.get(i-1).addActionListener(this);
            //mainPanel.add(buttons.get(i-1));
            add(buttons.get(i-1));
        }
        
        this.buttonHold = new JButton("HOLD");
        this.buttonHold.setBounds(0,((numFloor+1)/2)*35, 125, 60);
        add(buttonHold);
        this.buttonOpen = new JButton("OPEN");
        this.buttonOpen.setBounds(0,((numFloor+1)/2)*35 + 65, 125, 60);
        add(buttonOpen);
        this.buttonClose = new JButton("CLOSE");
        this.buttonClose.setBounds(0,((numFloor+1)/2)*35 + 65*2, 125, 60);
        add(buttonClose);
        //mainPanel.add(panel1);
        //mainPanel.add(panel2);
        //add(mainPanel);
        setVisible(true);
        setSize(141,((numFloor+1)/2)*35 + 65*3 + 35);
        
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println("Button pressed!");
        for(int i = 1; i <= this.numFloor; i++){
            if(cmd.equals(Integer.toString(i))){
                this.getButtons().get(i-1).setBackground(Color.red);
                //sent message to elevator controler
            }
            
        }
    }
    
    public void turnOff(String nameButton){
        if(nameButton.equals("HOLD")){
            this.getButtonHold().setBackground(null);
        } else if(nameButton.equals("CLOSE")){
            this.getButtonClose().setBackground(null);
        } else if(nameButton.equals("OPEN")){
            this.getButtonOpen().setBackground(null);
        } else {
            for(int i = 1; i <= this.numFloor; i++){
                if(nameButton.equals(Integer.toString(i))){
                    this.getButtons().get(i-1).setBackground(null);      
                }
            }
        }
    }

    public void turnOn(String nameButton){
        if(nameButton.equals("HOLD")){
            this.getButtonHold().setBackground(Color.yellow);
        } else if(nameButton.equals("CLOSE")){
            this.getButtonClose().setBackground(Color.yellow);
        } else if(nameButton.equals("OPEN")){
            this.getButtonOpen().setBackground(Color.yellow);
        } else {
            for(int i = 1; i <= this.numFloor; i++){
                if(nameButton.equals(Integer.toString(i))){
                    this.getButtons().get(i-1).setBackground(Color.red);      
                }
            }
        }
    }
    
    public ArrayList<JButton> getButtons() {
        return buttons;
    }
    
    public void setButtons(ArrayList<JButton> buttons) {
        this.buttons = buttons;
    }

    public JButton getButtonHold() {
        return buttonHold;
    }

    public void setButtonHold(JButton buttonHold) {
        this.buttonHold = buttonHold;
    }

    public JButton getButtonOpen() {
        return buttonOpen;
    }

    public void setButtonOpen(JButton buttonOpen) {
        this.buttonOpen = buttonOpen;
    }

    public JButton getButtonClose() {
        return buttonClose;
    }

    public void setButtonClose(JButton buttonClose) {
        this.buttonClose = buttonClose;
    }

    public int getNumFloor() {
        return numFloor;
    }

    public void setNumFloor(int numFloor) {
        this.numFloor = numFloor;
    }

    private Object getContentPane() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setDefaultCloseOperation(int DISPOSE_ON_CLOSE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Elevator getElevator() {
        return elevator;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }
    
    public void update(Elevator elevator){
        this.setElevator(elevator);
        this.turnOn(Integer.toString(this.getElevator().getTargetFloor()));
        if(this.getElevator().getCurrentFloor() != this.getElevator().getTargetFloor()){
            this.turnOff(Integer.toString(this.getElevator().getCurrentFloor()));
        }
    }
    
}
