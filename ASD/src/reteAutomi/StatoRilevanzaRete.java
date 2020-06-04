package reteAutomi;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@SuppressWarnings("restriction")
public class StatoRilevanzaRete {

    private static AtomicInteger ai =  new AtomicInteger(0);
    private int id;
    // <nomeLink, Evento>
    private ArrayList<Pair<String, Evento>> contenutoLinks;
    // <nomeAutoma, nomeStato>
    private ArrayList<Pair<String, String>> statiCorrentiAutomi;
    private ArrayList<String> decorazione;

    /**
     * Crea uno stato partendo dalla rete di automi
     * Inizializza la lista stati con gli stati correnti dei singoli automi
     * Inizializza la lista eventi con il contenuto dei lnk (eventualmente null)
     * @param ra la rete di automi di cui si vuole creare lo stato
     */
    public StatoRilevanzaRete(ReteAutomi ra, ArrayList<String> decorazione) {
        this.id = ai.incrementAndGet();
        this.contenutoLinks = new ArrayList<>();
        this.statiCorrentiAutomi = new ArrayList<>();
        this.aggiungiContenutiLinks(ra.getLinks());
        this.aggiungiStatiCorrenti(ra.getAutomi());
        this.decorazione = decorazione;
    }

    /**
     * costruttore preciso

     * @param contenutoLinks
     * @param statiAutomi
     * @param decorazione
     */
    public StatoRilevanzaRete( ArrayList<Pair<String, Evento>> contenutoLinks, ArrayList<Pair<String, String>> statiAutomi, ArrayList<String> decorazione) {
        this.id = ai.incrementAndGet();

        this.contenutoLinks = contenutoLinks;
        this.statiCorrentiAutomi = statiAutomi;
        this.decorazione = decorazione;
    }

    public StatoRilevanzaRete() {
        this.id = ai.incrementAndGet();

    }

    private void aggiungiContenutiLinks(ArrayList<Link> links) {
        for (Link link : links) {
            if (!link.isVuoto()){
                this.contenutoLinks.add(new Pair<>(link.getNome(), link.getEvento()));
            }
            else contenutoLinks.add(new Pair<>(link.getNome(), null));
        }
    }

    private void aggiungiStatiCorrenti(ArrayList<Automa> automi) {
        for (Automa automa : automi) {
            statiCorrentiAutomi.add(new Pair<>(automa.getNome(), automa.getStatoCorrente().getNome()));
        }
    }

    /**
     * se gia' presente viene ignorato -almeno cosi sembra nella slide 16, f viene incontrata piu' volte ma aggiunta solo la prima-
     * @param  etichetta String
     */
    public void addEtichettaRilevanzaToDecorazione(String etichetta){
        if (!this.decorazione.contains(etichetta)){
            this.decorazione.add(etichetta);
        }

    }





    public String getInfoStatoToString(){
        StringBuilder sb = new StringBuilder();

        for (Pair<String, String> coppiaAutomaStato : statiCorrentiAutomi) {
            sb.append("Stato dell'automa " + coppiaAutomaStato.getKey() + ": " + coppiaAutomaStato.getValue() + "\n");
        }

        for (Pair<String, Evento> contenutoLink : contenutoLinks) {
            if (!isNull(contenutoLink.getValue())) {
                sb.append("Contenuto del link " + contenutoLink.getKey() + ": " + contenutoLink.getValue() + "\n");
            }
            else sb.append("Contenuto del link " + contenutoLink.getKey() + ": null\n");
        }
        sb.append("Decorazione: " + decorazione + "\n");
        return sb.toString();
    }
    
    public ArrayList<Pair<String, Evento>> getContenutoLinks(){
    	return this.contenutoLinks;
    }
    
    public ArrayList<Pair<String, String>> getStatiCorrentiAutoma(){
    	return this.statiCorrentiAutomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatoRilevanzaRete)) return false;
        StatoRilevanzaRete that = (StatoRilevanzaRete) o;
        return this.id == that.getId();
    }
    
    public boolean equals(StatoRilevanzaRete s) {
    	return this.id == s.getId();
    }

    public int getId() {
        return id;
    }


    public ArrayList<String> getDecorazione() {
        return this.decorazione;
    }
}
