package views.graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class ElevatorButtonsFrame extends JFrame {

    private static final long serialVersionUID = -8699676873901285644L;
    public static int frame_width;
    public static int frame_height;
    private SimulationPanel panel;
    
    public ElevatorButtonsFrame(int elevator_count, int floor_count) {
        frame_width = (elevator_count * CabinElevator.ELEVATOR_WIDTH);
        frame_height = (floor_count) * CabinElevator.ELEVATOR_HEIGHT;
//    	System.out.println(frame_width+"x"+frame_height);
        setSize(frame_width, frame_height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        for (int i=0;i< elevator_count;i++)
            
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    panel.displayWaitingTime();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    panel.speedUp();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    panel.speedDown();
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    panel.restart();
                }
            }
            public void update(int num, int direct) {
            
            }
        });

//        Insets insets = getInsets(); 
//        frame_width = getWidth()+insets.left+insets.right;
//        frame_height = getHeight()+insets.bottom+insets.top;
//		setSize(frame_width, frame_height);
//		System.out.println(frame_width+"x"+frame_height);
    }

    

}
