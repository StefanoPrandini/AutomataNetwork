package reteAutomi;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class StatoRilevanzaReteAutomi {

    private static AtomicInteger ai =  new AtomicInteger(0);
    private int id;
    private String nome;

    private ArrayList<Link> links;
    private ArrayList<Automa> automi;
    private ArrayList<String> etichetteRilevanzaIncontrate;






    /**
     * Crea uno stato partendo dalla rete di automi
     * Inizializza la lista stati con gli stati correnti dei singoli automi
     * Inizializza la lista eventi con il contenuto dei lnk (eventualmente null)
     * @param ra la rete di automi di cui si vuole creare lo stato
     */
    public StatoRilevanzaReteAutomi(String nome, ReteAutomi ra) {
        this.id = ai.incrementAndGet();
        this.nome = nome;
        this.automi = new ArrayList<>();
        this.links = new ArrayList<>();
        automi.addAll(ra.getAutomi());
        links.addAll(ra.getLinks());
    }

    /**
     * aggiungo anche se gia presente, non viene specificato nelle slide
     * @param e
     */
    public void addEtichettaRilevanza(String e){
        this.etichetteRilevanzaIncontrate.add(e);
    }





    public String getInfoStato(){
        StringBuilder sb = new StringBuilder();
        for (Automa automa : automi) {
            sb.append("Stato corrente automa ").append(automa.getId()).append(": ").append(automa.getStatoCorrente().getId()).append("\n");
        }
        for (Link link : links) {
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
        return this.id == that.getId();
    }

    public int getId() {
        return id;
    }






}
