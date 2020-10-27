package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Terna implements Serializable {
	private static final long serialVersionUID = 5575599391009567691L;
	
	private Set<StatoRilevanzaRete> insiemeI;
    private StatoDizionario statoDizionario;
    private Set<Set<String>> diagnosi;

    private String nome;

    public Terna(String nome, Set<StatoRilevanzaRete> insiemeI, StatoDizionario statoCorrenteDizionario, Set<Set<String>> diagnosi) {
        this.insiemeI = insiemeI;
        this.statoDizionario = statoCorrenteDizionario;
        this.diagnosi = diagnosi;
        this.nome = nome;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terna)) return false;
        Terna terna = (Terna) o;
        return insiemeI.equals(terna.insiemeI) &&
                statoDizionario.equals(terna.statoDizionario) &&
                diagnosi.equals(terna.diagnosi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insiemeI, statoDizionario, diagnosi);
    }

    @Override
    public String toString() {
        return nome + ": (" + toStringInsiemeI() + ", " + toStringStatoDizionario() + ", " + toStringDiagnosi() +  ")";
    }


    /*
    * metodi per gestire eventualmente i toString in modo preciso in base al contenuto
    */
    private String toStringDiagnosi() {
        return diagnosi.toString();
    }

    private String toStringStatoDizionario() {
        return statoDizionario.getRidenominazione();
    }


    /**
     *
     * @return
     */
    private String toStringInsiemeI() {
        StringBuilder sb =new StringBuilder();
        sb.append("{");
        for (StatoRilevanzaRete statoRilevanzaRete : insiemeI) {
            sb.append(statoRilevanzaRete.getRidenominazione() + ", ");
        }
        // se c'e' altro oltre alla prima graffa vuol dire che sono stati inseriti elementi e anche virgola alla fine
        if(sb.length()>1) {
			sb.delete(sb.length()-2, sb.length());
		}
        sb.append("}");

        return sb.toString();
    }


    public Set<StatoRilevanzaRete> getInsiemeI() {
        return insiemeI;
    }

    public void setInsiemeI(Set<StatoRilevanzaRete> insiemeI) {
        this.insiemeI = insiemeI;
    }

    public StatoDizionario getStatoDizionario() {
        return statoDizionario;
    }

    public void setStatoDizionario(StatoDizionario statoCorrenteDizionario) {
        this.statoDizionario = statoCorrenteDizionario;
    }

    public Set<Set<String>> getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(Set<Set<String>> diagnosi) {
        this.diagnosi = diagnosi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void aggiornaTerna(Set<StatoRilevanzaRete> insiemeI, StatoDizionario statoCorrenteDizionario, Set<Set<String>> diagnosi){
        this.setInsiemeI(insiemeI);
        this.setStatoDizionario(statoCorrenteDizionario);
        this.setDiagnosi(diagnosi);
    }
}
