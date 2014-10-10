package views.graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MyFrame extends JFrame {

    private static final long serialVersionUID = -8699676873901285644L;
    public static int frame_width;
    public static int frame_height;
    private SimulationPanel panel;

    public MyFrame(int elevator_count, int floor_count) {
        int a = FixedFloor.FLOOR_WIDTH;
        frame_width = (elevator_count * AnimatedElevator.ELEVATOR_WIDTH) + (2 * FixedFloor.FLOOR_WIDTH);
        frame_height = (floor_count) * AnimatedElevator.ELEVATOR_HEIGHT;
//    	System.out.println(frame_width+"x"+frame_height);
        setSize(frame_width, frame_height);
        this.setTitle("[ICTK56]BailamICT03 : Elevator System View");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new SimulationPanel(this, getWidth(), getHeight());
        add(panel);
        pack();
        setResizable(true);
        setVisible(true);

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
        });

//        Insets insets = getInsets(); 
//        frame_width = getWidth()+insets.left+insets.right;
//        frame_height = getHeight()+insets.bottom+insets.top;
//		setSize(frame_width, frame_height);
//		System.out.println(frame_width+"x"+frame_height);
    }

    public void addAnimatedObject(AnimatedObject o) {
        panel.addAnimatedObject(o);
    }

    public void addFixedObject(FixedObject o) {
        panel.addFixedObject(o);
    }

}
