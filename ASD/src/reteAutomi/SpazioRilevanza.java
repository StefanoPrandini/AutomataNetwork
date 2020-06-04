package reteAutomi;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SpazioRilevanza {

    private ArrayList<StatoRilevanzaRete> statiRete;
    private StatoRilevanzaRete statoCorrente;


    //forse sono ridondanti ?
    private ArrayList<String> insiemeEtichetteRilevanza;
    private ArrayList<String> insiemeEtichetteOsservabilita;

    /*
   traiettoria è insieme delle transizioni che portano dallo stato iniziale allo stato in questione
   Qual è la soluzione migliore?
   1) una lista con gli id delle transizioni attivate
   2) una lista di transizioni vere e proprie
   3) mettere la lista degli stati nello spazio di rilevanza, tenendo quindi una mappa stato-traiettoria
   4) creare un oggetto traiettoria se servirà un ampliamento

   qui uso soluzione super easy
   */
    private LinkedHashMap< StatoRilevanzaRete, Transizione> mappaStatoRilevanzaTransizioni;

    /*
    costruzione iniziale dello spazio di rilevanza
    */
    public SpazioRilevanza(ArrayList<StatoRilevanzaRete> statiRete, ArrayList<String> insiemeEtichetteRilevanza, ArrayList<String> insiemeEtichetteOsservabilita) {
        this.statiRete = statiRete;
        this.insiemeEtichetteRilevanza = insiemeEtichetteRilevanza;
        this.insiemeEtichetteOsservabilita = insiemeEtichetteOsservabilita;
        this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>();
    }





    public void calcolaNuovoStato(){

    }


    /**
     * teoricamente, eseguire ripetutamente questa funzione dovrebbe portare alla costruzione completa dello spazio di rilevanza della rete di automi
     */
    public void eseguiPassaggioStato(){
        //TODO



        //passa in stato destinazione, aggiorna stato corrente del singolo componente, controlla se ci sono etichette coinvolte
    }


    public void inizializzaSpazioRilevanza(){
        this.insiemeEtichetteRilevanza = new ArrayList<>();
        this.insiemeEtichetteOsservabilita = new ArrayList<>();
        //this.statoCorrente;
    }

    /**
     * se una transizione risulta osservabile posso aggiungere l'etichetta di osservabilità
     * è richiesto un algoritmo che ridenomini gli stati dello spazio di rilevanza (non modifica lo spazio) vedi pagg.17-20 pdf
     * in ogni caso serve far vedere all'utente la corrispondenza tra uno stato dello spazio di rilevanza e il contenuto (getContenuto di StatoSpazioRilevanza)
     */
    public void etichettaturaOsservabilitaSpazio(){

        //TODO

    }

}
