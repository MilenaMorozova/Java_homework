package sample;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class ElevatorController{
    private int numberOfFloors;
    private int numberOfElevators;

    private GUI gui;

    protected ArrayList<Passenger> passengers;
    ArrayList<Elevator> elevators;

    public ElevatorController(GUI gui, int numberOfElevators, int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;

        this.gui = gui;
        this.gui.init(numberOfElevators, numberOfFloors);
        this.gui.createRoot();

        passengers = new ArrayList<Passenger>();
        elevators = new ArrayList<Elevator>();
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(this,6));
        }
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfElevators() {
        return numberOfElevators;
    }

    protected void addPassengerInList(Passenger passenger){
        passengers.add(passenger);
        gui.addPassengerInList(passenger);
    }

    protected void removePassengerFromList(Passenger passenger, Elevator elevator){
        passengers.remove(passenger);
        gui.addPassengerInElevator(passenger, elevators.indexOf(elevator));
    }

    protected void removePassengerFromElevator(Passenger passenger, Elevator elevator){
        gui.removePassengerFromElevator(elevators.indexOf(elevator), passenger);
    }

    private Elevator findNearestElevator(Passenger passenger){
        int min = numberOfFloors;
        Elevator nearestElevator = null;

        for(Elevator elevator: elevators){
            if(elevator.isEmpty() == false){
                continue;
            }
            if(elevator.getDirection() == Direction.STOP){
                int absDifference = Math.abs(passenger.getCurrentFloor() - elevator.getCurrentFloor());
                if(min > absDifference){
                    nearestElevator = elevator;
                    min = absDifference;
                }
            }
        }
        return nearestElevator;
    }

    private void processRequest(Passenger passenger, Elevator elevator) {
        elevator.setTargetFloor(passenger.getCurrentFloor());
        passenger.changeState();
        gui.runElevator(elevators.indexOf(elevator), elevator);
    }

    public void mainloop() {
        new PassengerGenerator(this).start();
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                Passenger passenger;
                Elevator elevator;
                for(int i = passengers.size()-1; i >= 0; i--){
                    passenger = passengers.get(i);
                    if(passenger.isWaiting()){
                        continue;
                    }
                    elevator = findNearestElevator(passenger);
                    if(elevator != null){
                        processRequest(passenger, elevator);
                    }
                }
            }
        }.start();
    }
}
