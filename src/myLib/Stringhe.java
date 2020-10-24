package myLib;

import java.io.File;

public class Stringhe {
    public static final String FILE_BASE_IN_CARTELLA = "I file JSON delle reti di esempio sono: \n";
    public static final String FILE_SESSIONI = "File salvati in sessioni precedenti: \n";
    public static final String EXAMPLE_PATH = System.getProperty("user.dir")+ File.separator + "JSON";
    public static final String SAVES_PATH = System.getProperty("user.dir") + File.separator + "sessioni";
    public static final String SAVE_FOLDER = System.getProperty("user.dir") + File.separator + "sessioni" + File.separator;
    public final static String INSERIRE_PERCORSO_FILE = "Inserisci l'indice del file di esempio desiderato o un altro percorso:\n-->";
    public static final String INSERISCI_SESSIONE = "Inserisci il nome del file:\n-->";
    public static final String LUNGHEZZA_PREFISSO = "Inserisci la lunghezza del prefisso (un numero negativo per tornare indietro): ";
    public static final int VALORE_USCITA = 0;
    public final static String INSERISCI_PERCORSO_OSSERVAZIONE = "Inserisci il percorso del file contenente l'osservazione (0 per uscire):\n-->";
    public final static String INFO_SPAZIO_RILEVANZA = "\tLo spazio di rilevanza ha %s stati e %s transizioni";
    public static final String INFO_RIDENOMINAZIONE_SPAZIO = "(StatoRilevanza) --> ridenominazione";
    public static final String INFO_MAPPA_SPAZIO = "[(StatoRilevanza partenza) -> Transizione -> (StatoRilevanza arrivo)]:";
    public static final String INSERIMENTO_OSSERVAZIONE = "Inserisci le etichette di un'osservazione lineare separandole con virgole " +
            "(es: o3, o4, act, cls), una stringa vuota per tornare indietro:\n-->";
    public static final String RISULTATO_RICERCA = "\n\tOsservazione lineare %s\n\tDiagnosi %s";
    public static final String NESSUN_RISULTATO = "\n\tL'osservazione lineare non corrisponde a nessuna traiettoria nella rete!";
    public static final String NESSUNO_SPAZIO_RILEVANZA = "Nessuno spazio di rilevanza rilevato, non e' possibile proseguire";
    public static final String INFO_I_O = "\tStato %s -> Input: %s, Output: %s";
    public static final String INFO_COPPIE_I_O = "\tStato %s -> coppie I/O: %s";
    public static final String VUOI_USCIRE = "Vuoi terminare il programma? y/n\n-->";
    public static final String NON_VALIDA = "Attenzione!\nNon e' stata inserita una risposta valida";
    public static final String CALCOLO_SPAZIO = "Calcolo dello spazio di rilevanza in corso\n";
    public static final String CALCOLO_SPAZIO_COMPLETO = "\nCalcolo completato, premi INVIO per proseguire ";
    public static final String CALCOLO_DIZIONARIO = "Costruzione del dizionario in corso\n";
    public static final String COSTRUZIONE_DIZIONARIO_COMPLETA = "\nCostruzione completa, premi INVIO per proseguire: ";
    public static final String RICERCA_COMPLETA = "\nRicerca completa, premi INVIO per proseguire: ";
    public static final String RICERCA_IN_CORSO = "Ricerca nel dizionario in corso\n";
    public static final String INSERISCI_PER_INTERROMPERE = "Inserisci '%s' per interrompere: ";
    public static final String SEI_SICURO = "Vuoi continuare comunque? y/n\n--> ";
    public static final String CARICAMENTO_ANNULLATO = "Caricamento annullato";
    public static final String CONTINUA_CALCOLO_DIZ = "Continua con il calcolo di un dizionario o il caricamento di un diverso spazio";
    public static final String STOP = "stop";
    public static final String OK = "ok";
    public static final String STRINGA_VUOTA = "";
    public static final String OSS_LIN_IN_MEMORIA = "E' presente in memoria questa osservazione lineare:\n%s";
    public static final String ESTENSIONE_OSSERVAZIONE = "Aggiungi una o piu' etichette alla sequenza (es. o2, o3), inserisci una stringa vuota per tornare indietro\n-->";
    public static final String INFO_DIZIONARIO = "Il dizionario ha %s stati e %s transizioni etichettate";
    public static final String RISULTATO_TERNE = "\nRisultato di monitoraggio e revisione da %s: \n";
    public static final String INDICE_INDIETRO = "0 Indietro\n";
    public static final String MONITORAGGIO_REVISIONE = "monitoraggio e revisione";
    public static final String RICERCA_DIAGNOSI = "ricerca e diagnosi";

    /**
    *
    *
    *                  ESTENSIONI
    *
    */

    public static final String ESTENSIONE_SPAZIO = ".spazio.ser";
    public static final String ESTENSIONE_DIZ = ".diz.ser";
    public static final String ESTENSIONE_RETE = ".rete.ser";
    public static final String ESTENSIONE_OSS_LIN_RIC = ".linossric.ser";
    public static final String ESTENSIONE_OSS_LIN_MON = ".linossmon.ser";
    public static final String ESTENSIONE_AUTOMA_OSS = ".autoss.ser";
    public static final String SALVATAGGIO_OK = "E' stato creato il file %s";
    public static final String ERRORE_SALVATAGGIO = "C'e' stato un problema nel salvataggio";


    /**
     *
     *
     *                  TITOLI
     *
     */

    public static final String TITOLO_INIZIALE = "Elaboratore per reti di automi";
    public static final String TITOLO_GESTIONE_RETE = "Gestione rete di automi";
    public static final String TITOLO_CARICAMENTO_DIZIONARIO = "Caricamento dizionario da file";
    public static final String TITOLO_SOVRASCRIVI = "Attenzione! Un dizionario e' gia'� presente, procedendo sovrascriverai i dati";
    public static final String TITOLO_CALCOLO_DIZIONARIO = "Calcolo di un dizionario";
    public static final String TITOLO_GESTIONE_DIZIONARIO_PARZIALE = "Calcolo di un dizionario parziale";
    public static final String TITOLO_GESTIONE_DIZIONARIO = "Gestione dizionario";
    public static final String TITOLO_INFORMAZIONI = "Informazioni...";
    public static final String TITOLO_INFO_SPAZIO_R = "Informazioni spazio di rilevanza";
    public static final String TITOLO_INFO_DIZIONARIO = "Informazioni dizionario";
    public static final String TITOLO_RICERCA_DIZIONARIO = "Ricerca nel dizionario - Diagnosi";
    public static final String TITOLO_MONITORAGGIO = "Monitoraggio e revisione";
    public static final String TITOLO_ESTENSIONE = "Estensione dinamica del dizionario";
    public static final String TITOLO_SALVA = "Salvataggio risultati";
    public static final String TITOLO_CARICAMENTI = "Caricamento da una sessione precedente";
    public static final String INPUT_OUTPUT = "Input e Output:";
    public static final String COPPIE_INPUT_OUTPUT = "Coppie I/O:";


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
            "Calcola da Spazio Rilevanza salvato in precedenza"
    };

    public static final String[] OPZIONI_GESTIONE_DIZIONARIO_PARZIALE = {
            "Calcolo da prefisso",
            "Calcolo da osservazione"
    };


    public static final String[] OPZIONI_INFORMAZIONI = {
            "Informazioni sulla rete di automi",
            "Informazioni sullo spazio di rilevanza",
            "Informazioni sul dizionario"
    };

    public static final String[]  OPZIONI_GESTIONE_DIZIONARIO = {
            "Informazioni",
            "Ricerca nel dizionario - Diagnosi",
            "Monitoraggio con revisione",
            "Estendi dizionario",
            "Salva",
            "Carica",
            "Chiudi elaboratore"
    };

    public static final String[] OPZIONI_INFO_SPAZIO_R = {
            "Vedi dettagli degli stati",
            "Vedi ridenominazione",
            "Vedi mappa dello spazio"
    };

    public static final String[] OPZIONI_INFO_DIZIONARIO = {
            "Vedi dettagli degli stati del dizionario",
            "Vedi ridenominazione del dizionario",
            "Vedi informazioni su I/O"
    };

    public static final String[] OPZIONI_RICERCA_DIZIONARIO = {
            "Inserisci osservazione lineare nuova ed effettua ricerca",
            "Aggiungi etichette ad un'osservazione precedente ed effettua ricerca",
            "Vedi diagnosi precedente"
    };

    public static final String[] OPZIONI_MONITORAGGIO = {
            "Effettua monitoraggio e revisione",
            "Aggiungi etichette ad un'osservazione precedente ed effettua monitoraggio",
            "Vedi risultato precedente"
    };

    public static final String[] OPZIONI_SALVA = {
            "Salva rete di automi",
            "Salva spazio di rilevanza",
            "Salva dizionario",
            "Salva osservazione lineare per la ricerca",
            "Salva osservazione lineare per il monitoraggio",
            "Salva automa osservazione"
    };

    public static final String[] OPZIONI_CARICAMENTI = {
            "Caricamento osservazione lineare per ricerca",
            "Caricamento osservazione lineare per monitoraggio",
            "Caricamento automa osservazione"
    };

    public static final String[] OPZIONI_ESTENSIONE = {
            "Espansione tramite un'osservazione",
    };

    public static final String[] RISPOSTE_VALIDE = {
            "si",
            "sì",
            "si'",
            "s",
            "yes",
            "y",
            "no",
            "n"
    };


    /**
     *
     *
     *                  ERRORI
     *
     */

    public static final String ERRORE_JSON = "\nErrore JSON";
    public static final String ERRORE_FORMATTAZIONE = "\nFormattazione JSON errata";
    public static final String ERRORE_FILEPATH = "\nFile non trovato";
    public static final String CARICAMENTO_RIUSCITO = "\nCaricamento riuscito";
    public static final String CARICAMENTO_RIUSCITO_CON_NOME = "\nCaricamento di %s riuscito";
    public static final String CARICAMENTO_IN_CORSO = "Caricamento di %s in corso";
    public static final String CARICAMENTO_OSS_LIN = "Osservazione lineare caricata: %s";
    public static final String NESSUNA_RETE_CARICATA = "\nAttenzione!\nNon e' stata caricata alcuna rete";
    public static final String NESSUNA_OSSERVAZIONE = "Attenzione!\nNon e' presente alcuna osservazione da estendere";
    public static final String NESSUNA_OSSERVAZIONE_INSERITA = "Attenzione!\nNon e' stata ancora inserita un'osservazione per %s";
    public final static String ERRORE_CARICAMENTO = "Errore nel caricamento";
    public static final String ESTENSIONE_NON_VALIDA = "Attenzione!\nFornire un file con estensione %s" ;
    public static final String ERRORE_CLASSE = "Classe non valida";
    public static final String NOMI_RETE_DIVERSI = "Attenzione!\nLo spazio di rilevanza indicato potrebbe non essere relativo alla rete caricata!";
    public static final String INFO_NOMI_RETE = "La rete di automi fornita e' chiamata %s, lo spazio di rilevanza e' relativo alla rete %s";



}
