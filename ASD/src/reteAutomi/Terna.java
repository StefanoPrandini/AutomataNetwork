package reteAutomi;

import java.util.Objects;

public class Terna<P, S, T> {

    private P primo;
    private S secondo;
    private T terzo;

    public Terna(P primo, S secondo, T terzo) {
        this.primo = primo;
        this.secondo = secondo;
        this.terzo = terzo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terna)) return false;
        Terna<?, ?, ?> terna = (Terna<?, ?, ?>) o;
        return Objects.equals(getPrimo(), terna.getPrimo()) &&
                Objects.equals(getSecondo(), terna.getSecondo()) &&
                Objects.equals(getTerzo(), terna.getTerzo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrimo(), getSecondo(), getTerzo());
    }

    @Override
    public String toString() {
        return "Terna (" + toStringPrimo() + ", " + toStringSecondo() + ", " + toStringTerzo() +  ")";
    }



    /*
    * metodi per gestire eventualmente i toString in modo preciso in base al contenuto
    */
    private String toStringTerzo() {
        return terzo.toString();
    }

    private String toStringSecondo() {
        return secondo.toString();
    }

    private String toStringPrimo() {
        return primo.toString();
    }



    public P getPrimo() {
        return primo;
    }

    public void setPrimo(P primo) {
        this.primo = primo;
    }

    public S getSecondo() {
        return secondo;
    }

    public void setSecondo(S secondo) {
        this.secondo = secondo;
    }

    public T getTerzo() {
        return terzo;
    }

    public void setTerzo(T terzo) {
        this.terzo = terzo;
    }
}
