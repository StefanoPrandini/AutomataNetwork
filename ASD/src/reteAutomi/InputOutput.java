package reteAutomi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class InputOutput implements Serializable {


    private ReteAutomi rete;
    private Automa osservazione;
    private ArrayList<String> osservazioneLineare;
    private boolean daOsservazione;
    private boolean ricerca;
    private int distanzaMax;
    private SpazioRilevanza sr;
    private Dizionario diz;

    public Dizionario getDiz() {
        return diz;
    }

    public void setDiz(Dizionario diz) {
        this.diz = diz;
    }

    private Set<Set<String>> risultatoRicerca;

    public Set<Set<String>> getRisultatoRicerca() {
        return risultatoRicerca;
    }

    public void setRisultatoRicerca(Set<Set<String>> risultatoRicerca) {
        this.risultatoRicerca = risultatoRicerca;
    }

    public ReteAutomi getRete() {
        return rete;
    }

    public Automa getOsservazione() {
        return osservazione;
    }

    public boolean isDaOsservazione() {
        return daOsservazione;
    }

    public int getDistanzaMax(){
        return distanzaMax;
    }

    public SpazioRilevanza getSpazioRilevanza(){
        return sr;
    }


    public void setRete(ReteAutomi rete) {
        this.rete = rete;
    }

    public void setOsservazione(Automa osservazione) {
        this.osservazione = osservazione;
    }

    public void setDaOsservazione(boolean daOsservazione) {
        this.daOsservazione = daOsservazione;
    }

    public void setDistanzaMax(int distanzaMax) {
        this.distanzaMax = distanzaMax;
    }

    public void setSr(SpazioRilevanza sr) {
        this.sr = sr;
    }

    public void setRicerca(boolean ricerca){
        this.ricerca = ricerca;
    }

    public boolean isRicerca() {
        return this.ricerca;
    }

    public ArrayList<String> getOsservazioneLineare() {
        return osservazioneLineare;
    }

    public void setOsservazioneLineare(ArrayList<String> osservazioneLineare) {
        this.osservazioneLineare = osservazioneLineare;
    }
}
