package models;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import logger.LoggingCenter;
/**
 *
 * @author x_nem
 */
public class Controller {
    // List lifts the controler
    private ArrayList<Elevator> elevators = null;

    private ArrayList<FloorButton> floorButtons = null;
    private ArrayList<ElevatorButton> elevatorButtons = null;

    public ArrayList<ElevatorButton> getElevatorButtons() {
        return elevatorButtons;
    }

    public void setElevatorButtons(ArrayList<ElevatorButton> elevatorButtons) {
        this.elevatorButtons = elevatorButtons;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }
    
    private ArrayList<Request> requests = null;
    public void addReqests(Request request){
        
    }
    public ArrayList<FloorButton> getFloorButtons() {
        return floorButtons;
    }

    public void setFloorButtons(ArrayList<FloorButton> floorButtons) {
        this.floorButtons = floorButtons;
    }

    // Number of floor of the controler
    private int floorCount;

    /**
     * Constructor controler
     *
     * @param floor_count Levels
     * @param elevators_list
     * @param passengers_list Passenger List
     * @param floorButtons_list
     */
    public Controller(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        constructor(floor_count, elevators_list, passengers_list, floorButtons_list);
    }

    /**
     * Constructor controler with a parameter
     *
     * @param floor_count
     */
    public Controller(int floor_count) {
        constructor(floor_count, new ArrayList<Elevator>(floor_count), new LinkedList<Passenger>(), new ArrayList<FloorButton>(floor_count*2-2));
    }

    /**
     * Private method used by manufacturers to initialize variables
     *
     * @ Param floor_count Levels
     * @ Param list elevators_list Elevator (Elevator to list)
     * @ Param passengers_list Passengers List
     */
    private void constructor(int floor_count, ArrayList<Elevator> elevators_list, LinkedList<Passenger> passengers_list, ArrayList<FloorButton> floorButtons_list) {
        this.floorCount = floor_count;
        this.elevators = elevators_list;
        
        this.floorButtons = floorButtons_list;
        this.requests = new ArrayList<Request>();
    }

    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }
    
    public void getPassengerInform(Button b) {
        if (b instanceof FloorButton) {
            b.turnOn();
            addReqests(((FloorButton)b).getFloorNum(), ((FloorButton)b).isDirection());
        }
        else {          
            Elevator belongEle = ((ElevatorButton)b).getBelongElevator();
            int nextFloor = ((ElevatorButton)b).getNumFloor();
            boolean goUp  = belongEle.isGoingToTop();
            if ((nextFloor > belongEle.getCurrentFloor()) && goUp){
                LoggingCenter.getInstance().logger.info("Controller " +  this + ": got an passenger's inform by button " + b);
                belongEle.addIRequest(nextFloor);
                b.turnOn();
                return;
            }
            if ((nextFloor < belongEle.getCurrentFloor()) && !goUp){
                LoggingCenter.getInstance().logger.info("Controller " +  this + ": got an passenger's inform by button " + b);
                belongEle.addIRequest(nextFloor);
                b.turnOn();
                return;
            }
        }
    }
    
    public void turnOffElevatorButton(Elevator el, String btnName) {
        for (ElevatorButton eb : elevatorButtons) {
            if (eb.belongElevator == el && Integer.toString(eb.getNumFloor()).equals(btnName)) {
                eb.turnOff();
            }
        }
    }
    
    /**
     * Returns the number of floors (excluding DRC)
     *
     * @return
     */
    public int getFloorCount() {
        return floorCount;
    }

//    /**
//     * Returns the number of floors + the DRC
//     *
//     * @return
//     */
//    public int getFloorCount() {
//        return floorCount + 1;
//    }

    public int getElevatorCount() {
        return elevators.size();
    }

    /**
     * Retournet the first elevator which is upstairs in parameter If there has
     * not the function returns null
     *
     * @param floor
     * @return
     */
    public Elevator getElevatorAtFloor(int floor) {
        for (Elevator e : elevators) {
            if (e.getCurrentFloor() == floor) {
                return e;
            }
        }
        return null;
    }

    void addReqests(int floorNum, boolean direction) {
        Request newRequest = new Request(floorNum, direction);
        for (Request request: requests)
            if (request.equals(newRequest))
                return;
        this.requests.add(new Request(floorNum, direction));
        this.processNewRequest();
    }
    
    void processNewRequest(){
        Request lastRequest =  this.requests.get(this.requests.size()-1);
        for (Elevator elevator : elevators){
            //List<Request> eleRequests = elevator.getRequests();
            if (elevator.getCurrentFloor() < lastRequest.getStartFloor())
                if (elevator.isGoingToTop()){
                    elevator.addRequests(lastRequest);
                    return;
                }
             if (elevator.getCurrentFloor() > lastRequest.getStartFloor())
                if (!elevator.isGoingToTop()){
                    elevator.addRequests(lastRequest);
                    return;
                }
        }
        int best = 10000;        
        int numEle = 0;
        for (int i = 0; i < elevators.size(); i++)
            if (elevators.get(i).getMustOpenAt().isEmpty())
            if (abs(elevators.get(i).getCurrentFloor()-lastRequest.startFloor) < best){
                best = abs(elevators.get(i).getCurrentFloor()-lastRequest.startFloor);
                numEle = i;
            }
        elevators.get(numEle).addRequests(lastRequest);
       // Console.debug(elevators.size() +  "  fdd\n");
        //elevators.get(rand.nextInt(elevators.size()-1)).addRequests(lastRequest);            
    }

    void startRequest(Request request) {
        getFloorButtons().get(request.getStartFloor()*2 + (request.isDirection()?1:0)-1).turnOff();
        requests.remove(request);
    }

}
