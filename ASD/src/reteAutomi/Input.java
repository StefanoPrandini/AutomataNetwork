package reteAutomi;

public class Input {


    private ReteAutomi rete;
    private Automa osservazione;
    private boolean daOsservazione;
    private int distanzaMax;
    private SpazioRilevanza sr;


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
}
