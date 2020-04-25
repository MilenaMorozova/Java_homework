package sample;

import java.util.Random;


public class PassengerGenerator extends Thread{
    private int maxFloor;
    private ElevatorController elevatorController;

    public PassengerGenerator(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
        maxFloor = elevatorController.getNumberOfFloors();
    }

    private void generate() {
        Random random = new Random();
        int currentFloor = random.nextInt(maxFloor) + 1;
        int toFloor = random.nextInt(maxFloor) + 1 ;
        if (toFloor != currentFloor) {
            int ImageNumber = random.nextInt(5);
            elevatorController.addPassengerInList(new Passenger(currentFloor, toFloor, ImageNumber));
        }
    }

    @Override
    public void run() {
        while(true) {
            generate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
