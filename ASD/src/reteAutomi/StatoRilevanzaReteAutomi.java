package reteAutomi;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class StatoRilevanzaReteAutomi {

    private static AtomicInteger ai =  new AtomicInteger(0);
    private int id;
    private String nome;

    private ArrayList<Pair<Integer, Evento>> contenutoLinks;
    private ArrayList<Pair<Integer, Integer>> statiCorrentiAutomi;
    private ArrayList<String> decorazione;






    /**
     * Crea uno stato partendo dalla rete di automi
     * Inizializza la lista stati con gli stati correnti dei singoli automi
     * Inizializza la lista eventi con il contenuto dei lnk (eventualmente null)
     * @param ra la rete di automi di cui si vuole creare lo stato
     */
    public StatoRilevanzaReteAutomi(String nome, ReteAutomi ra, ArrayList<String> decorazione) {
        this.id = ai.incrementAndGet();
        this.nome = nome;
        this.contenutoLinks = new ArrayList<>();
        this.statiCorrentiAutomi = new ArrayList<>();
        this.aggiungiContenuti(ra.getLinks());
        this.aggiungiStatiCorrenti(ra.getAutomi());
        this.decorazione = decorazione;
    }



    private void aggiungiContenuti(ArrayList<Link> links) {
        for (Link link : links) {
            if (!link.isVuoto()){
                this.contenutoLinks.add(new Pair<>(link.getId(), link.getEvento()));
            }
            else contenutoLinks.add(new Pair<>(link.getId(), null));
        }
    }

    private void aggiungiStatiCorrenti(ArrayList<Automa> automi) {
        for (Automa automa : automi) {
            statiCorrentiAutomi.add(new Pair<>(automa.getId(), automa.getStatoCorrente().getId()));
        }
    }

    /**
     * aggiungo anche se gia presente, non viene specificato nelle slide
     * @param e
     */
    public void addEtichettaRilevanzaToDecorazione(String e){
        this.decorazione.add(e);
    }





    public String getInfoStatoToString(){
        StringBuilder sb = new StringBuilder();

        for (Pair<Integer, Integer> coppiaAutomaStato : statiCorrentiAutomi) {
            sb.append("Stato dell'automa " + coppiaAutomaStato.getKey() + ": " + coppiaAutomaStato.getValue() + "\n");
        }

        for (Pair<Integer, Evento> contenutoLink : contenutoLinks) {
            if (!isNull(contenutoLink.getValue())) {
                sb.append("Contenuto del link " + contenutoLink.getKey() + ": " + contenutoLink.getValue() + "\n");
            }
            else sb.append("Contenuto del link " + contenutoLink.getKey() + ": " + null + "\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatoRilevanzaReteAutomi)) return false;
        StatoRilevanzaReteAutomi that = (StatoRilevanzaReteAutomi) o;
        return this.id == that.getId();
    }
    
    public boolean equals(StatoRilevanzaReteAutomi s) {
    	return this.id == s.getId();
    }

    public int getId() {
        return id;
    }






}
