package reteAutomi;

import javafx.util.Pair;
import java.util.*;

@SuppressWarnings("restriction")
public class StatoRilevanzaRete {

    // <nomeLink, Evento>
    private ArrayList<Pair<String, Evento>> contenutoLinks;
    // <nomeAutoma, nomeStato>
    private ArrayList<Pair<String, String>> statiCorrentiAutomi;
    private Set<String> decorazione;
    private String ridenominazione;

    /**
     * Crea uno stato partendo dalla rete di automi
     * Inizializza la lista stati con gli stati correnti dei singoli automi
     * Inizializza la lista eventi con il contenuto dei lnk (eventualmente null)
     * @param ra la rete di automi di cui si vuole creare lo stato
     */
    public StatoRilevanzaRete(ReteAutomi ra, Set<String> decorazione) {
        this.contenutoLinks = new ArrayList<>();
        this.statiCorrentiAutomi = new ArrayList<>();
        this.aggiungiContenutiLinks(ra.getLinks());
        this.aggiungiStatiCorrenti(ra.getAutomi());
        this.decorazione = decorazione;
    }
    
    
    public ArrayList<Pair<String, Evento>> getContenutoLinks(){
    	return this.contenutoLinks;
    }
    
    
    public ArrayList<Pair<String, String>> getStatiCorrentiAutoma(){
    	return this.statiCorrentiAutomi;
    }
    
    
    public Set<String> getDecorazione() {
        return this.decorazione;
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
     * decorazione e' un insieme (set): add aggiunge etichetta solo se non e' gia' presente
     * @param  etichetta String
     */
    public void addEtichettaRilevanzaToDecorazione(String etichetta){
        this.decorazione.add(etichetta);

    }
    
    
    /**
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
    */
    
    
    public boolean equals(StatoRilevanzaRete s) {
    	return this.contenutoLinks.equals(s.getContenutoLinks()) && this.statiCorrentiAutomi.equals(s.statiCorrentiAutomi) && this.decorazione.equals(s.decorazione);
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatoRilevanzaRete)) return false;
        StatoRilevanzaRete that = (StatoRilevanzaRete) o;
        return this.contenutoLinks.equals(that.getContenutoLinks()) && this.statiCorrentiAutomi.equals(that.statiCorrentiAutomi) && this.decorazione.equals(that.decorazione);
    }


    @Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + ((contenutoLinks == null) ? 0 : contenutoLinks.hashCode());
		result = prime * result + ((statiCorrentiAutomi == null) ? 0 : statiCorrentiAutomi.hashCode());
		result = prime * result + ((decorazione == null) ? 0 : decorazione.hashCode());

		return result;
	}

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("(");
    	
    	for(Pair<String, String> statiCorrenti : statiCorrentiAutomi) {
    		sb.append(statiCorrenti.getKey() + ":" + statiCorrenti.getValue() + ", ");
    	}
    	for(Pair<String, Evento> eventiLink : contenutoLinks) {
    		if(eventiLink.getValue()==null) {
    			sb.append(eventiLink.getKey() + ":null, ");
    		}
    		else {
        		sb.append(eventiLink.getKey() + ":" + eventiLink.getValue().getNome() + ", ");
    		}
    	}
    	sb.append("decorazione:" + decorazione);
    	sb.append(")");
    	return sb.toString();
    }

    public String getRidenominazione() {
        return ridenominazione;
    }

    public void setRidenominazione(String ridenominazione) {
        this.ridenominazione = ridenominazione;
    }
}
