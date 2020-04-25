package sample;

import java.util.ArrayList;

enum Direction{UP, DOWN, STOP};

public class Elevator {
    private int spaciousness;
    private int currentFloor = 1;
    private int targetFloor = 0;
    protected ArrayList<Passenger> passengers;
    private Direction direction = Direction.STOP;
    private ElevatorController elevatorController;

    public Elevator(ElevatorController elevatorController, int spaciousness) {
        this.elevatorController = elevatorController;
        passengers = new ArrayList<Passenger>();
        this.spaciousness = spaciousness;
    }

    public boolean isFull(){
        return passengers.size() == spaciousness;
    }

    public boolean isEmpty(){
        return passengers.isEmpty();
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    protected boolean addPassenger(Passenger passenger){
        if(passengers.add(passenger)) {
            elevatorController.removePassengerFromList(passenger, this);
            return true;
        }
        return false;
    }

    protected void removePassenger(Passenger passenger){
        passengers.remove(passenger);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    protected void changeDirection(){
        if(targetFloor != 0) {
            if (targetFloor - currentFloor > 0) {
                direction = Direction.UP;
            }
            else if(targetFloor - currentFloor < 0){
                direction = Direction.DOWN;
            }
        }
        else{
            if(isEmpty()) {
                direction = Direction.STOP;
            } else {
                direction = passengers.get(0).identifyDirection();
            }
        }
    }

    protected void decreaseCurrentFloor(){
        currentFloor -= 1;
    }

    protected void increaseCurrentFloor(){
        currentFloor += 1;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    protected boolean addPassengersInElevator() {
        if(currentFloor == targetFloor){
            targetFloor = 0;
        }
        for (int i = 0; i < elevatorController.passengers.size(); i++) {
            if (isFull()) {
                break;
            }
            Passenger passenger = elevatorController.passengers.get(i);
            if (passenger.getCurrentFloor() != currentFloor) {
                continue;
            }
            if(targetFloor != 0){
                //проверяю могу ли я подвезти человека, когда лифт едет к целевому человеку
                //человек должен выходить раньше целевого этажа или на целевом этаже
                if(passenger.identifyDirection() == direction){
                    switch (direction) {
                        case UP:
                            if (targetFloor >= passenger.getToFloor()) {
                                return addPassenger(passenger);
                            }
                            break;
                        case DOWN:
                            if (targetFloor <= passenger.getToFloor()) {
                                return addPassenger(passenger);
                            }
                            break;
                    }
                }
            }
            else{
                if(isEmpty()) {
                    //беру целевого человека на этаже
                    return addPassenger(passenger);
                }else{
                    //беру тех людей, которых в том же направлении, что и целевому
                    if (passenger.identifyDirection() == direction) {
                        return addPassenger(passenger);
                    }
                }
            }
        }
        return false;
    }

    protected boolean removePassengerFromElevator(){
        for(int i = 0; i < passengers.size(); i++){
            Passenger passenger = passengers.get(i);
            if(currentFloor == passenger.getToFloor()){
                removePassenger(passenger);
                elevatorController.removePassengerFromElevator(passenger, this);
                return true;
            }
        }
        return false;
    }
}
