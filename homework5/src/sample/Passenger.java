package sample;

public class Passenger {
    private int currentFloor;
    private int toFloor;
    private int imageViewNumber;

    private boolean isWaiting = false;

    public Passenger(int currentFloor, int toFloor, int imageViewNumber) {
        this.currentFloor = currentFloor;
        this.toFloor = toFloor;
        this.imageViewNumber = imageViewNumber;
    }

    public int getToFloor(){
        return toFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getImageViewNumber() {
        return imageViewNumber;
    }

    protected void changeState(){isWaiting = !isWaiting;}

    public boolean isWaiting(){return isWaiting;};

    public Direction identifyDirection(){
        if(toFloor - currentFloor > 0){
            return Direction.UP;
        }
        return Direction.DOWN;
    }
}
