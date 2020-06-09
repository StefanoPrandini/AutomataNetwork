package reteAutomi;

import java.util.Set;

public class TernaMonitoraggio extends Terna {


    // insieme I della terna, stato corrente del dizionario, diagnosi DELTA della terna

    // insieme I: insieme degli stati contenuti nella chiusura silenziosa relativa allo stato stesso, in cui entra una trnasizione osservabile proveniente
    // da uno stato di rilevanza contenuto in un'altra chiusura silenziosa, corrispondente ad un altro stato del dizionario
    private Set<StatoRilevanzaRete> insiemeI;

    private StatoRilevanzaReteDeterminizzata statoCorrenteDizionario;

    private Set<Set<String>> diagnosi;

    public TernaMonitoraggio(Set<StatoRilevanzaRete> insiemeI, StatoRilevanzaReteDeterminizzata statoCorrenteDizionario, Set<Set<String>> diagnosi) {
        super(insiemeI, statoCorrenteDizionario, diagnosi);
    }


}
