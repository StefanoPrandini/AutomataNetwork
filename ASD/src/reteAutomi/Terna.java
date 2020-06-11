package reteAutomi;

import java.util.Objects;
import java.util.Set;

public class Terna {

    private Set<StatoRilevanzaRete> insiemeI;
    private StatoDizionario statoCorrenteDizionario;
    private Set<Set<String>> diagnosi;

    private String nome;

    public Terna(String nome, Set<StatoRilevanzaRete> insiemeI, StatoDizionario statoCorrenteDizionario, Set<Set<String>> diagnosi) {
        this.insiemeI = insiemeI;
        this.statoCorrenteDizionario = statoCorrenteDizionario;
        this.diagnosi = diagnosi;
        this.nome = nome;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terna)) return false;
        Terna terna = (Terna) o;
        return insiemeI.equals(terna.insiemeI) &&
                statoCorrenteDizionario.equals(terna.statoCorrenteDizionario) &&
                diagnosi.equals(terna.diagnosi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insiemeI, statoCorrenteDizionario, diagnosi);
    }

    @Override
    public String toString() {
        return nome + " (" + toStringInsiemeI() + ", " + toStringStatoCorrenteDizionario() + ", " + toStringDiagnosi() +  ")";
    }


    /*
    * metodi per gestire eventualmente i toString in modo preciso in base al contenuto
    */
    private String toStringDiagnosi() {
        return diagnosi.toString();
    }

    private String toStringStatoCorrenteDizionario() {
        return statoCorrenteDizionario.getRidenominazione();
    }


    /**
     * c'è una virgola in piu'
     * @return
     */
    private String toStringInsiemeI() {
        StringBuilder sb =new StringBuilder();
        sb.append("{");
        for (StatoRilevanzaRete statoRilevanzaRete : insiemeI) {
            sb.append(statoRilevanzaRete.getRidenominazione() + ",");
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

    public StatoDizionario getStatoCorrenteDizionario() {
        return statoCorrenteDizionario;
    }

    public void setStatoCorrenteDizionario(StatoDizionario statoCorrenteDizionario) {
        this.statoCorrenteDizionario = statoCorrenteDizionario;
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
        this.setStatoCorrenteDizionario(statoCorrenteDizionario);
        this.setDiagnosi(diagnosi);
    }
}
