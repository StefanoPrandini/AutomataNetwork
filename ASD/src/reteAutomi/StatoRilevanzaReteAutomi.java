package reteAutomi;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class StatoRilevanzaReteAutomi {

    private static AtomicInteger ai =  new AtomicInteger(0);
    private int id;

    private Map<Link, Evento> mappaLinkEventi;
    private Map<Automa, Stato> mappaAutomiStati;
    private ArrayList<String> etichetteRilevanzaIncontrate;
    private ArrayList<String> etichetteOsservabilitaIncontrate;





    /**
     * Crea uno stato partendo dalla rete di automi
     * Inizializza la lista stati con gli stati correnti dei singoli automi
     * Inizializza la lista eventi con il contenuto dei lnk (eventualmente null)
     * @param ra la rete di automi di cui si vuole creare lo stato
     */
    public StatoRilevanzaReteAutomi(ReteAutomi ra) {
        this.id = ai.incrementAndGet();
        this.mappaAutomiStati = new LinkedHashMap<>();
        this.mappaLinkEventi = new LinkedHashMap<>();
        for (Automa automa : ra.getAutomi()) {
           mappaAutomiStati.put(automa, automa.getStatoCorrente());
        }


        for (Link link : ra.getLinks()) {
            mappaLinkEventi.put(link, link.getEvento());
        }
    }

    /**
     * aggiungo anche se gia presente, non viene specificato nelle slide
     * @param e
     */
    public void addEtichettaRilevanza(String e){
        this.etichetteRilevanzaIncontrate.add(e);
    }

    /**
     * aggiungo anche se gia presente, non viene specificato nelle slide
     * @param e
     */
    public void addEtichettaOsservabilita(String e){
        this.etichetteOsservabilitaIncontrate.add(e);
    }




    public String getInfoStato(){
        StringBuilder sb = new StringBuilder();
        for (Automa automa : mappaAutomiStati.keySet()) {
            sb.append("Stato corrente automa ").append(automa.getId()).append(": ").append(automa.getStatoCorrente().getId()).append("\n");
        }
        for (Link link : mappaLinkEventi.keySet()) {
            if (!isNull(link.getEvento())){
                sb.append("Evento su link ").append(link.getId()).append(": ").append(link.getEvento().getId()).append("\n");
            }
            else sb.append("Evento su link ").append(link.getId()).append(": null ").append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatoRilevanzaReteAutomi)) return false;
        StatoRilevanzaReteAutomi that = (StatoRilevanzaReteAutomi) o;
        return  Objects.equals(mappaLinkEventi, that.mappaLinkEventi) &&
                Objects.equals(mappaAutomiStati, that.mappaAutomiStati) &&
                Objects.equals(etichetteRilevanzaIncontrate, that.etichetteRilevanzaIncontrate) &&
                Objects.equals(etichetteOsservabilitaIncontrate, that.etichetteOsservabilitaIncontrate);
    }



    public ArrayList<Evento> getAllEventi() {
       return new ArrayList<>(mappaLinkEventi.values());
    }


    public ArrayList<Stato> getAllStatiCorrenti() {
        return new ArrayList<>(mappaAutomiStati.values());
    }




}
