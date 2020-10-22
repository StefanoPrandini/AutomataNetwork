package reteAutomi;

public abstract class Algoritmo implements Runnable {

    private boolean inInterruzione = false;
    private boolean terminato = false;


    public void stop(){
        inInterruzione = true;
    }

    public boolean isInInterruzione(){
        return inInterruzione;
    }

    public boolean isTerminato() {
        return terminato;
    }

    public void setTerminato(boolean terminato){
        this.terminato = terminato;
    }
}
