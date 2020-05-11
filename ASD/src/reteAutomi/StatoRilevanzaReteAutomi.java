package reteAutomi;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StatoRilevanzaReteAutomi {

    private static AtomicInteger ai =  new AtomicInteger(0);
    private int id;

    private ArrayList<Evento> eventi;
    private ArrayList<Stato> stati;
    private ArrayList<String> etichetteRilevanzaIncontrate;
    private ArrayList<String> etichetteOsservabilitaIncontrate;



    /**
     * Uno StatoRilevanzaReteAutomi rappresenta lo stato di una rete di automi in un determinato momento
     * Lo stato iniziale della rete è dato dallo stato dei componenti nello stato iniziale, tutti i link vuoti e la lista di etichette vuota
     * @param eventi il contenuto dei link della rete, anche eventualmente vuoto
     * @param stati gli stati correnti degli automi nella rete
     */
    public StatoRilevanzaReteAutomi(ArrayList<Evento> eventi, ArrayList<Stato> stati, ArrayList<String> etichetteRilevanzaIncontrate, ArrayList<String> etichetteOsservabilitaIncontrate ) {
        this.id = ai.incrementAndGet();
        this.eventi = eventi;
        this.stati = stati;
        this.etichetteRilevanzaIncontrate = etichetteRilevanzaIncontrate;
        this.etichetteOsservabilitaIncontrate = etichetteRilevanzaIncontrate;
    }







    public ArrayList<Evento> getEventi() {
        return eventi;
    }

    public void setEventi(ArrayList<Evento> eventi) {
        this.eventi = eventi;
    }

    public ArrayList<Stato> getStati() {
        return stati;
    }

    public void setStati(ArrayList<Stato> stati) {
        this.stati = stati;
    }
}
