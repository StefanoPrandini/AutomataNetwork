package reteAutomi;

public abstract class Algoritmo implements Runnable {

    private boolean inInterruzione = false;

    public void stop(){
        inInterruzione = true;
    }

    public boolean isInInterruzione(){
        return inInterruzione;
    }

}
