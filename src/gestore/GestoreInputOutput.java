package gestore;

import algoritmo.Dizionario;
import model.Automa;
import model.ReteAutomi;
import algoritmo.SpazioRilevanza;

import java.io.Serializable;
import java.util.ArrayList;

public class GestoreInputOutput implements Serializable {

    private ReteAutomi rete;
    private Automa osservazione;
    private ArrayList<String> osservazioneLineareRicerca;
    private ArrayList<String> osservazioneLineareMonitoraggio;
    private boolean daOsservazione;
    private boolean ricerca;
    private boolean monitoraggio;
    private int distanzaMax;
    private SpazioRilevanza sr;
    private Dizionario diz;
    private ArrayList<String> logMonitoraggio;

    private static final long serialVersionUID = 2441396588518329859L;


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

    public boolean isMonitoraggio() {
        return monitoraggio;
    }

    public void setMonitoraggio(boolean monitoraggio) {
        this.monitoraggio = monitoraggio;
    }

    public ArrayList<String> getOsservazioneLineareRicerca() {
        return osservazioneLineareRicerca;
    }

    public ArrayList<String> getOsservazioneLineareMonitoraggio() {
        return osservazioneLineareMonitoraggio;
    }

    public void setOsservazioneLineareRicerca(ArrayList<String> osservazioneLineareRicerca) {
        this.osservazioneLineareRicerca = osservazioneLineareRicerca;
    }

    public void setOsservazioneLineareMonitoraggio(ArrayList<String> osservazioneLineareMonitoraggio) {
        this.osservazioneLineareMonitoraggio = osservazioneLineareMonitoraggio;
    }

    public Dizionario getDizionario() {
        return diz;
    }

    public void setDizionario(Dizionario diz){
        this.diz = diz;
    }

    public void inizializzaLogMonitoraggio(){
        this.logMonitoraggio = new ArrayList<>();
    }

    public void addEventoToLog(String evento){
        this.logMonitoraggio.add(evento);
    }

    public ArrayList<String> getLogMonitoraggio(){
        return this.logMonitoraggio;
    }

}
