package myLib;

import java.io.File;

public class Stringhe {
    public static final String SAVE_FOLDER = "ASD/sessioni";
    public final static String INSERIRE_PERCORSO_FILE = "Inserisci il percorso del file:\n-->";
    public static final String COSTRUZIONE_S_R = "Costruzione spazio rilevanza";
    public static final String COSTRUZIONE_DIZIONARIO = "Costruzione dizionario";
    public static final String FINITO = "Finito";
    public static final String LUNGHEZZA_PREFISSO = "Inserisci la lunghezza del prefisso (un numero negativo per tornare indietro): ";
    public static final int VALORE_USCITA = 0;
    public final static String INSERISCI_PERCORSO_OSSERVAZIONE = "Inserisci il percorso del file contenente l'osservazione (0 per uscire):\n-->";
    public final static String INFO_SPAZIO_RILEVANZA = "Lo spazio di rilevanza ha %s stati";
    public static final String INFO_MAPPA_SPAZIO = "[(StatoRilevanza partenza) -> Transizione -> (StatoRilevanza arrivo)]:";
    public static final String INSERIMENTO_OSSERVAZIONE = "Inserisci le etichette di un'osservazione lineare separandole con virgole " +
            "(es: o3, o4, o21):\n-->";
    public static final String RISULTATO_RICERCA = "Osservazione lineare %s -> %s";
    public static final String RISULTATO_MONITORAGGIO = "Risultato di monitoraggio e revisione: ";
    public static final String NESSUN_RISULTATO = "Nessuna corrispondenza trovata";
    public static final String NESSUNO_SPAZIO_RILEVANZA = "Nessuno spazio di rilevanza rilevato, non è possibile proseguire";


    /**
     *
     *
     *                  TITOLI
     *
     */

    public static final String TITOLO_INIZIALE = "Elaboratore per reti di automi";
    public static final String TITOLO_GESTIONE_RETE = "Gestione rete di automi";
    public static final String TITOLO_CARICAMENTO_DIZIONARIO = "Caricamento dizionario da file";
    public static final String TITOLO_SOVRASCRIVI = "Attenzione! Un dizionario è già presente, procedendo sovrascriverai i dati";
    public static final String TITOLO_CALCOLO_DIZIONARIO = "Calcolo di un dizionario";
    public static final String TITOLO_GESTIONE_DIZIONARIO_PARZIALE = "Calcolo di un dizionario parziale";
    public static final String TITOLO_GESTIONE_DIZIONARIO = "Gestione dizionario";
    public static final String TITOLO_INFO_SPAZIO_R = "Informazioni spazio di rilevanza";
    public static final String TITOLO_INFO_DIZIONARIO = "Informazioni dizionario";
    public static final String TITOLO_RICERCA_DIZIONARIO = "Ricerca nel dizionario";
    public static final String TITOLO_MONITORAGGIO = "Monitoraggio e revisione";


    /**
     *
     *
     *                  OPZIONI
     *
     */

    public static final String[] OPZIONI_MENU_CARICAMENTO = {
            "Carica una nuova rete di automi da un file JSON",
            "Carica una rete da una sessione di lavoro precedente"
    };

    public static final String[] OPZIONI_GESTIONE_RETE = {
            "Informazioni rete",
            "Calcola dizionario",
            "Carica dizionario"
    };

    public static final String[] OPZIONI_CARICAMENTO_DIZIONARIO = {
            "Carica dizionario da file"
    };

    public static final String[] OPZIONI_SOVRASCRIVI = {

    };
    public static final String[] OPZIONI_CALCOLO_DIZIONARIO = {

            "Calcola dizionario completo (include calcolo dello Spazio di Rilevanza)",
            "Calcola dizionario parziale (include calcolo dello Spazio di Rilevanza)",
            "Calcola da Spazio Rilevanza"
    };

    public static final String[] OPZIONI_GESTIONE_DIZIONARIO_PARZIALE = {
            "Calcolo da prefisso",
            "Calcolo da osservazione"
    };

    public static final String[]  OPZIONI_GESTIONE_DIZIONARIO = {
            "Informazioni sullo spazio di rilevanza",
            "Informazioni sul dizionario",
            "Ricerca nel dizionario",
            "Monitoraggio con revisione",
            "Estendi dizionario",
            "Salva spazio di rilevanza",
            "Salva il dizionario"

    };

    public static final String[] OPZIONI_INFO_SPAZIO_R = {
            "Vedi dettagli degli stati",
            "Vedi ridenominazione",
            "Vedi mappa dello spazio"
    };

    public static final String[] OPZIONI_INFO_DIZIONARIO = {
            "Vedi dettagli degli stati del dizionario",
            "Vedi ridenominazione del dizionario"
    };

    public static final String[] OPZIONI_RICERCA_DIZIONARIO = {
            "Inserisci osservazione lineare",
            "Vedi risultato precedente"
    };

    public static final String[] OPZIONI_MONITORAGGIO = {
            "Effettua monitoraggio e revisione",
            "Vedi risultato precedente"
    };






    /**
     *
     *
     *                  ERRORI
     *
     */


    public static final String ERRORE_JSON = "Errore JSON";
    public static final String ERRORE_FORMATTAZIONE = "Formattazione JSON errata";
    public static final String ERRORE_FILEPATH = "File non trovato";
    public static final String CARICAMENTO_RIUSCITO = "Caricamento riuscito";
    public static final String CARICAMENTO_RIUSCITO_CON_NOME = "Caricamento di %s riuscito";
    public static final String NESSUNA_RETE_CARICATA = "Attenzione!\nNon è stata caricata alcuna rete";
    public final static String ERRORE_CARICAMENTO = "Errore nel caricamento";





















    public static final String[] OPZIONI_SOTTO_MENU_DIZIONARIO_COMPLETO = {
            "Costruisci dizionario completo",
            "Informazioni dizionario",
            "Informazioni spazio di rilevanza"
    };
    public static final String TITOLO_SOTTO_MENU_DIZIONARIO_COMPLETO = "Dizionario completo";
    public static final String NESSUN_DIZIONARIO = "Non è ancora stato costruito alcun dizionario";
    public static final String NESSUN_S_R = "Non è ancora stato costruito alcuno spazio di rilevanza";


    public static final String[] OPZIONI_SOTTO_MENU_MONITORAGGIO = {
            "Inserisci un'ossservazione lineare",
            "Effetta il monitoraggio con revisione",

    };
    public static final String TITOLO_SOTTO_MENU_MONITORAGGIO = "Monitoraggio + revisione e gestione terne";


    public static final String[] OPZIONI_MENU_10 = {
            "Carica sessione",
            "Elimina sessione",

    };
    public static final String TITOLO_MENU_10 = "Carica/elimina sessioni esistenti";
    public static final String INSERISCI_SESSIONE = "Inserisci il nome del file:\n-->";

    public static final String NESSUNA_OSSERVAZIONE = "Attenzione! Non è stata caricata alcuna osservazione";
    public static final String OSSERVAZIONE_NON_CORRISPONDE = "L'osservazione inserita non corrisponde a nessuna traiettoria della rete";
    public static final String[] OPZIONI_MENU_PREFISSO = {
            "Inserisci lunghezza prefisso",
            "Costruisci prefisso del dizionario"
    };
    public static final String TITOLO_MENU_PREFISSO = "Prefisso del dizionario";
    public static final String LEGGI_INTERO = "Inserisci la lunghezza desiderata (-1 per esplorazione completa)\n-->";
    public static final String SALVATO = "Salvato";





    public static final String[] OPZIONI_INIZIALI = {
            "Carica nuova rete di automi da file",
            "Carica nuova osservazione",
            "Dizionario completo",
            "Monitoraggio",
            "Prefisso",
            "Dizionari parziali",
            "Estensione dizionario",
            "Riassunto automi",
            "Salva sessione",
            "Carica sessione"
    };


    /**
     * INUTILITA'
     */

    /**


     System.out.println("Working Directory = " + System.getProperty("user.dir"));

     System.out.println(files[0]);

     Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

     System.out.println(path);

     **/
}
