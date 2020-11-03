package algoritmo;

public abstract class Algoritmo implements Runnable {

    private boolean inInterruzione = false;
    private boolean terminato = false;
    private boolean ricercaTerminata = false;

    private long inizio;
    private long fine;

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

    public long getInizio() {
        return inizio;
    }

    public void setInizio(long inizio) {
        this.inizio = inizio;
    }

    public long getFine() {
        return fine;
    }

    public void setFine(long fine) {
        this.fine = fine;
    }

    public long tempoEsecuzione(){
        return getFine()-getInizio();
    }
}
