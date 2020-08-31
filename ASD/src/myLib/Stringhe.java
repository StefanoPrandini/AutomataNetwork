package myLib;

public class Stringhe {
    public final static String INSERIRE_PERCORSO_FILE = "Inserisci il percorso del file:\n-->";
    public final static String ERRORE_CARICAMENTO = "Errore nel caricamento";
    public static final String COSTRUZIONE_S_R = "Costruzione spazio rilevanza";
    public static final String COSTRUZIONE_DIZIONARIO = "Costruzione dizionario";


    public static final String[] OPZIONI = {
            "Carica nuova rete di automi da file",
            "Carica nuova osservazione",
            "Dizionario completo",
            "Monitoraggio",
            "Prefisso",
            "Dizionarii parziali",
            "Estensione dizionario",
            "Riassunto automi",
            "Salva sessione",
            "Carica sessione"
    };

    public static final String TITOLO = "Elaboratore per reti di automi";
    public static final String SAVE_FOLDER = "ASD/sessioni";

    /**
     *
     * Errori in caricamento files json
     *
     */
    public static final String ERRORE_JSON = "Errore JSON";
    public static final String ERRORE_FORMATTAZIONE = "Formattazione JSON errata";
    public static final String ERRORE_FILEPATH = "File non trovato";
    public static final String CARICAMENTO_RIUSCITO = "Caricamento di %s riuscito";
    public static final String NESSUNA_RETE_CARICATA = "Attenzione!\nNon è stata caricata alcuna rete";


    public static final String FINITO = "Finito";




    public static final String[] OPZIONI_SOTTO_MENU_DIZIONARIO_COMPLETO = {
            "Costruisci dizionario completo",
            "Informazioni dizionario",
            "Informazioni spazio di rilevanza"
    };
    public static final String TITOLO_SOTTO_MENU_DIZIONARIO_COMPLETO = "Dizionario completo";
    public static final String NESSUN_DIZIONARIO = "Non è ancora stato costruito alcun dizionario";
    public static final String NESSUN_S_R = "Non è ancora stato costruito alcuno spazio di rilevanza";


    public static final String[] OPZIONI_SOTTO_MENU_MONITORAGGIO = {
            "Effetta il monitoraggio con revisione",
            "Inserisci un'ossservazione lineare",
            "Calcola terne"
    };
    public static final String TITOLO_SOTTO_MENU_MONITORAGGIO = "Monitoraggio, revisione e gestione terne";


    public static final String[] OPZIONI_MENU_10 = {
            "Carica sessione",
            "Elimina sessione",

    };
    public static final String TITOLO_MENU_10 = "Carica/elimina sessioni esistenti";
    public static final String INSERISCI_SESSIONE = "Inserisci il nome della sessione:\n";


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
