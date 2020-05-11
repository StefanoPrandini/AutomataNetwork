package reteAutomi;

import java.util.ArrayList;

public class SpazioRilevanza {

    private ArrayList<StatoRilevanzaReteAutomi> statiRete;


    //forse sono ridondanti ?
    private ArrayList<String> insiemeEtichetteRilevanza;
    private ArrayList<String> insiemeEtichetteOsservabilita;


    /*
    costruzione iniziale dello spazio di rilevanza
    */
    public SpazioRilevanza(ArrayList<StatoRilevanzaReteAutomi> statiRete, ArrayList<String> insiemeEtichetteRilevanza, ArrayList<String> insiemeEtichetteOsservabilita) {
        this.statiRete = statiRete;
        this.insiemeEtichetteRilevanza = insiemeEtichetteRilevanza;
        this.insiemeEtichetteOsservabilita = insiemeEtichetteOsservabilita;
    }


    /**
     * teoricamente, eseguire ripetutamente questa funzione dovrebbe portare alla costruzione completa dello spazio di rilevanza della rete di automi
     */
    public void eseguiPassaggioStato(){
        //TODO

        //passa in stato destinazione, aggiorna stato corrente del singolo componente, controlla se ci sono etichette coinvolte
    }

    /**
     * se una transizione risulta osservabile posso aggiungere l'etichetta di osservabilità
     * è richiesto un algoritmo che ridenomini gli stati dello spazio di rilevanza (non modifica lo spazio) vedi pagg.17-20 pdf
     * in ogni caso serve far vedere all'utente la corrispondenza tra uno stato dello spazio di rilevanza e il contenuto (getContenuto di StatoSpazioRilevanza)
     */
    public void etichettaturaOsservabilitaSpazio(){

    }

}
