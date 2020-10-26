package algoritmo;

public abstract class Algoritmo implements Runnable {

    private boolean inInterruzione = false;
    private boolean terminato = false;
    private boolean ricercaTerminata = false;

    public void stop(){
        inInterruzione = true;
    }

    public void inizializzaInInterruzione(){
        this.inInterruzione = false;
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

    public void setRicercaTerminata(boolean b){
        this.ricercaTerminata = b;
    }

    public boolean isRicercaTerminata(){
        return this.ricercaTerminata;
    }
}
