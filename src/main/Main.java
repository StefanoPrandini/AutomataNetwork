package main;

import input.GestoreDizionari;
import input.GestoreFile;
import javafx.util.Pair;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
import myLib.VerificaDati;
import reteAutomi.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static java.util.Objects.isNull;
import static myLib.InputDati.leggiStringa;


public class Main {


	private static ReteAutomi ra;
	private static Automa oss;
	private static SpazioRilevanza sr;
	private static Dizionario diz;
	private static ArrayList<String> osservazioneLineare = new ArrayList<>();
	private static Set<Set<String>> decorazione;
	private static int lunghezzaPrefisso = SpazioRilevanza.ESPLORAZIONE_COMPLETA;
	private static boolean spazioRilevanzaCalcolato = false;
	private static File[] filesSessione;
	private static File[] filesEsempio;


	public static void main(String[] args) {
		MyMenu m = new MyMenu(Stringhe.TITOLO_INIZIALE, Stringhe.OPZIONI_MENU_CARICAMENTO, true);
		int scelta = m.scegli();
		while (scelta !=0) {
			gestisciCaricamentoIniziale(scelta);
			scelta = m.scegli();
		}
	}

	/**
	 * Caricamento della rete iniziale
	 * @param scelta
	 */
	private static void gestisciCaricamentoIniziale(int scelta) {

		switch (scelta){
			case 0: break; //EXIT

			case 1:  { //carica da JSON
				System.out.println(Stringhe.FILE_BASE_IN_CARTELLA);
				stampaFileDiEsempio();
				GestoreFile gf = new GestoreFile();
				gf.setPathRete(determinaFilepath(inputNomeFileJSON()));
				try {
					ra = gf.caricaRete();
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, ra.getNome()));
					System.out.println(ra.toString());
					MyMenu menuGestioneRete = new MyMenu(Stringhe.TITOLO_GESTIONE_RETE, Stringhe.OPZIONI_GESTIONE_RETE);
					int sceltaGestioneRete = menuGestioneRete.scegli();
					while (sceltaGestioneRete != 0){
						gestisciRete(sceltaGestioneRete);
						sceltaGestioneRete = menuGestioneRete.scegli();
					}
				}
				catch (NullPointerException npe){
					System.out.println(Stringhe.ERRORE_CARICAMENTO);
				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_CARICAMENTO);
				}

				break;

			}
			case 2: { // carica sessione
				if(sessioniDisponibili()){
					visualizzaSessioniDisponibili();
					try {
						gestisciCaricamentoReteAutomi();
						System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, ra.getNome()));
						MyMenu menuGestioneRete = new MyMenu(Stringhe.TITOLO_GESTIONE_RETE, Stringhe.OPZIONI_GESTIONE_RETE);
						int sceltaGestioneRete = menuGestioneRete.scegli();
						while (sceltaGestioneRete != 0){
							gestisciRete(sceltaGestioneRete);
							sceltaGestioneRete = menuGestioneRete.scegli();
						}
					} catch (Exception e){
						System.out.println(Stringhe.ERRORE_CARICAMENTO);
					}
				}
				break;
			}
			default: break;
		}
	}



	private static void gestisciRete(int scelta){
		//informazioni rete, calcola dizionario, carica dizionario
		switch (scelta){
			case 0: { //EXIT //TODO sei sicuro?

				break;
			}
			case 1: { // informazioni rete
				System.out.println(ra.toString());
				break;
			}

			case 2: {//calcola dizionario
				MyMenu menuCalcolaDizionario = new MyMenu(Stringhe.TITOLO_CALCOLO_DIZIONARIO, Stringhe.OPZIONI_CALCOLO_DIZIONARIO);
				int sceltaCalcoloDizionario = menuCalcolaDizionario.scegli();
				while (sceltaCalcoloDizionario != 0){
					gestisciCalcoloDizionario(sceltaCalcoloDizionario);
					sceltaCalcoloDizionario = menuCalcolaDizionario.scegli();
				}
				break;
			}
			case 3:{//carica dizionario
				MyMenu menuCaricamentoDizionario = new MyMenu(Stringhe.TITOLO_CARICAMENTO_DIZIONARIO, Stringhe.OPZIONI_CARICAMENTO_DIZIONARIO);
				int sceltaCaricamentoDizionario = menuCaricamentoDizionario.scegli();
				while ( sceltaCaricamentoDizionario != 0){
					try {
						gestioneCaricamentoDizionario(sceltaCaricamentoDizionario);
						sceltaCaricamentoDizionario = menuCaricamentoDizionario.scegli();
					}catch (Exception e){
						System.out.println(Stringhe.ERRORE_CARICAMENTO);
					}
				}
				MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
				int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				while (sceltaGestioneDizionario != 0){
					gestisciDizionario(sceltaGestioneDizionario);
					sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				}
				break;
			}

			default: break;


		}

	}

	private static void gestisciCalcoloDizionario(int sceltaCalcolo) {
		switch (sceltaCalcolo){
			case 0: {//back
				break;
			}
			case 1:{//calcola dizionario completo
				diz = calcolaDizionario(SpazioRilevanza.ESPLORAZIONE_COMPLETA);
				MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
				int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				while (sceltaGestioneDizionario != 0){
					gestisciDizionario(sceltaGestioneDizionario);
					sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				}
				break;
			}
			case 2: {// calcola dizionario parziale
				MyMenu menuGestioneDizionarioParziale = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO_PARZIALE, Stringhe.OPZIONI_GESTIONE_DIZIONARIO_PARZIALE);
				int sceltaDizionarioParziale = menuGestioneDizionarioParziale.scegli();
				while (sceltaDizionarioParziale != 0){
					gestisciCalcoloDizionarioParziale(sceltaDizionarioParziale);
					sceltaDizionarioParziale = menuGestioneDizionarioParziale.scegli();
				}
				break;
			}
			case 3: { //calcola da spazio di rilevanza (da caricare)
				// --> qui carica lo spazio e cambia il valore del boolean spazioRilevanzaCalcolato, poi esce e si ritrova nello stesso menu con caricamento effettuato
				//
				visualizzaSpaziRilevanzaDisponibili();
				String filepath = InputDati.leggiStringa(Stringhe.INSERISCI_SESSIONE);
				filepath = Stringhe.SAVE_FOLDER + filepath;
				if ( ! filepath.contains(Stringhe.ESTENSIONE_SPAZIO) ){
					System.out.println(String.format(Stringhe.ESTENSIONE_NON_VALIDA, Stringhe.ESTENSIONE_SPAZIO));
					break;
				}
				File folder = new File(Stringhe.SAVES_PATH);
				File[] files = folder.listFiles();
				for (File file : files) {
					if (file.getPath().equals(filepath)){
						GestoreFile gf = new GestoreFile();
						try {

							System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
							SpazioRilevanza spazioRilevanza = gf.caricaSpazioRilevanza(filepath);
							boolean sovrascrive = true;
							if (spazioRilevanza.getHashRete() != ra.hashCode()){
								System.out.println(Stringhe.HASH_DIVERSI);
								System.out.println(String.format(Stringhe.INFO_HASH, ra.hashCode(), spazioRilevanza.getHashRete()));
								String vuoiUscire = InputDati.leggiStringa(Stringhe.SEI_SICURO);
								while ( ! rispostaValida(vuoiUscire) ){
									vuoiUscire = InputDati.leggiStringa(Stringhe.NON_VALIDA);
								}
								//se vuole inserire uno spazio di rilevanza non relativo alla rete
								if (rispostaNegativa(vuoiUscire)){
									sovrascrive = false;
								}

							}
							if (sovrascrive){
								sr = spazioRilevanza;
								System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, filepath));
								System.out.println(Stringhe.CONTINUA_CALCOLO_DIZ);
								spazioRilevanzaCalcolato = true;
							}

							else  {
								System.out.println(Stringhe.CARICAMENTO_ANNULLATO);
								spazioRilevanzaCalcolato = false;
							}
						} catch (ClassNotFoundException e){
							e.printStackTrace();
						}  catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}

				break;


			}
		}

	}

	private static void gestisciCalcoloDizionarioParziale(int scelta) {
		switch (scelta){
			case 0: {// exit
				break;
			}
			case 1:{// da prefisso

				int lettura = InputDati.leggiIntero(Stringhe.LUNGHEZZA_PREFISSO);
				if (lettura < Stringhe.VALORE_USCITA) break;
				diz = calcolaDizionario(lunghezzaPrefisso = lettura);
				MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
				int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				while (sceltaGestioneDizionario != 0){
					gestisciDizionario(sceltaGestioneDizionario);
					sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				}
				break;
			}
			case 2: { // da osservazione

				String percorsoOss = InputDati.leggiStringa(Stringhe.INSERISCI_PERCORSO_OSSERVAZIONE);
				if (percorsoOss.equals("" + Stringhe.VALORE_USCITA)) break;
				GestoreFile gf = new GestoreFile();
				gf.setPathOss(percorsoOss);
				try{

					oss = gf.caricaOsservazione();
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, oss.getNome()));
					System.out.println(oss.toStringOss());
					diz = calcolaDizionarioParzialeDaOsservazione();
					MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
					int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					while (sceltaGestioneDizionario != 0){
						gestisciDizionario(sceltaGestioneDizionario);
						sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					}
				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_CARICAMENTO);
					System.out.println(e);
				}

				break;
			}
		}
	}

	private static void gestisciDizionario(int scelta) {
			switch (scelta) {
				case 0: { //back
					break;
				}
				case 1: {//informazioni...
					MyMenu menuInformazioni = new MyMenu(Stringhe.TITOLO_INFORMAZIONI, Stringhe.OPZIONI_INFORMAZIONI);
					int sceltaInformazioni = menuInformazioni.scegli();
					while (sceltaInformazioni != 0){
						gestisciSceltaInfo(sceltaInformazioni);
						sceltaInformazioni = menuInformazioni.scegli();
					}
					break;
				}
				case 2: {//ricerca in dizionario
					MyMenu menuRicercaDizionario = new MyMenu(Stringhe.TITOLO_RICERCA_DIZIONARIO, Stringhe.OPZIONI_RICERCA_DIZIONARIO);
					int sceltaRicerca = menuRicercaDizionario.scegli();
					while (sceltaRicerca != 0){
						gestisciRicerca(sceltaRicerca);
						sceltaRicerca = menuRicercaDizionario.scegli();
					}
					break;
				}
				case 3: {//monitoraggio e revisione
					MyMenu menuMonitoraggioRevisione = new MyMenu(Stringhe.TITOLO_MONITORAGGIO, Stringhe.OPZIONI_MONITORAGGIO);
					int sceltaMonitoraggio = menuMonitoraggioRevisione.scegli();
					while (sceltaMonitoraggio != 0){
						gestisciMonitoraggio(sceltaMonitoraggio);
						sceltaMonitoraggio = menuMonitoraggioRevisione.scegli();
					}
					break;
				}
				case 4: {//estensione
					MyMenu menuEstensione = new MyMenu(Stringhe.TITOLO_ESTENSIONE, Stringhe.OPZIONI_ESTENSIONE);
					int sceltaEstensione = menuEstensione.scegli();
					while (sceltaEstensione != 0){
						gestisciEstensione(sceltaEstensione);
						sceltaEstensione = menuEstensione.scegli();
					}
					//TODO check con Ste se estensione funziona
					break;
				}

				case 5:  {//salvataggi
					MyMenu menuSalvataggi = new MyMenu(Stringhe.TITOLO_SALVA, Stringhe.OPZIONI_SALVA);
					int sceltaSalva = menuSalvataggi.scegli();
					while (sceltaSalva != 0){
						gestisciSalvataggio(sceltaSalva);
						sceltaSalva = menuSalvataggi.scegli();
					}
					break;
				}

				case 6:{// caricamento osservazioni in sessioni precedenti
					MyMenu menuCaricamento = new MyMenu(Stringhe.TITOLO_CARICAMENTI, Stringhe.OPZIONI_CARICAMENTI);
					int sceltaCaricamento = menuCaricamento.scegli();
					while (sceltaCaricamento != 0){
						try {
							gestisciCaricamento(sceltaCaricamento);
						} catch (Exception e) {
							System.out.println(Stringhe.ERRORE_FILEPATH);
						}
						sceltaCaricamento = menuCaricamento.scegli();
					}
					break;
				}
				case 7: { //chiudi elaboratore
					String vuoiUscire = InputDati.leggiStringa(Stringhe.VUOI_USCIRE);
					while ( ! rispostaValida(vuoiUscire) ){
						vuoiUscire = InputDati.leggiStringa(Stringhe.NON_VALIDA);
					}
					if (rispostaNegativa(vuoiUscire)){
						break;
					}
					else System.exit(0);

				}
			}
	}

	private static void gestisciRicerca(int sceltaRicerca) {
		switch (sceltaRicerca){
			case 0:{//back
				break;
			}
			case 1:{ //oss lineare da tastiera
				osservazioneLineare = new ArrayList<>();
				String input= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
				ArrayList<String> splitted = new ArrayList<>(Arrays.asList(input.split(",")));
				for (String s : splitted) {
					if ( ! s.equals(Stringhe.STRINGA_VUOTA)) osservazioneLineare.add(s.trim());

				}
				effettuaRicerca(osservazioneLineare);
				break;
			}

			case 2: {//estendi ricerca precedente
				if (isNull(osservazioneLineare)){
					System.out.println(Stringhe.NESSUNA_OSSERVAZIONE);
					break;
				}
				System.out.println(String.format(Stringhe.OSS_LIN_IN_MEMORIA, osservazioneLineare));
				String estensioneOss = InputDati.leggiStringa(Stringhe.ESTENSIONE_OSSERVAZIONE);
				ArrayList<String> splitted = new ArrayList<>(Arrays.asList(estensioneOss.split(",")));
				for (String s : splitted) {
					if ( ! s.equals(Stringhe.STRINGA_VUOTA)) osservazioneLineare.add(s.trim());
				}
				effettuaRicerca(osservazioneLineare);
				break;
			}

			case 3: {//vedi risultato precedente
				if ( isNull(decorazione)){
					System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				else System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineare, decorazione));
				break;
			}
		}
	}

	private static void gestisciSceltaInfo(int sceltaInformazioni) {
		switch (sceltaInformazioni){
			case 0: { //back
				break;
			}
			case 1: { //rete
				System.out.println(ra.toString());
				break;
			}
			case 2: { //spazio rilevanza
				MyMenu menuInfoSpazioRilevanza = new MyMenu(Stringhe.TITOLO_INFO_SPAZIO_R, Stringhe.OPZIONI_INFO_SPAZIO_R);
				int sceltaInfoSpazioR = menuInfoSpazioRilevanza.scegli();
				//TODO info rapide su numero stati ecc
				while (sceltaInfoSpazioR != 0){
					gestisciInfoSpazioRilevanza(sceltaInfoSpazioR);
					sceltaInfoSpazioR = menuInfoSpazioRilevanza.scegli();
				}
				break;
			}
			case 3: { // dizionario
				MyMenu menuInfoDizionario = new MyMenu(Stringhe.TITOLO_INFO_DIZIONARIO, Stringhe.OPZIONI_INFO_DIZIONARIO);
				int sceltaInfoDiz = menuInfoDizionario.scegli();
				//TODO info rapide su numero stati ecc
				while (sceltaInfoDiz != 0){
					gestisciInfoDizionario(sceltaInfoDiz);
					sceltaInfoDiz = menuInfoDizionario.scegli();
				}
				break;
			}

		}
	}

	private static void gestisciSalvataggio(int sceltaSalva) {
		switch (sceltaSalva){
			case 0: {//back
				break;
			}
			case 1: {//salva rete automi
				String nome = creaNomeFile() + Stringhe.ESTENSIONE_RETE;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
					oos.writeObject(ra);
					System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
				break;
			}
			case 2: {//salva spazio rilevanza
				String nome = creaNomeFile() + Stringhe.ESTENSIONE_SPAZIO;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
					oos.writeObject(sr);
					System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
				break;
			}
			case 3: {//salva dizionario
				String nome = creaNomeFile() + Stringhe.ESTENSIONE_DIZ;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
					oos.writeObject(diz);
					System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
				break;
			}
			case 4: {//salva osservazione lineare
				String nome = creaNomeFile() + Stringhe.ESTENSIONE_OSS_LIN;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
					oos.writeObject(osservazioneLineare);
					System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
				break;
			}
			case 5:{ //salva automa osservazione
				String nome = creaNomeFile() + Stringhe.ESTENSIONE_AUTOMA_OSS;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
					oos.writeObject(oss);
					System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
				}
				catch (Exception e){
					System.out.println(e.toString());
				}
				break;
			}
		}
	}

	private static void gestisciCaricamento(int sceltaCaricamento){

		switch (sceltaCaricamento){
			case 0:{//back
				break;
			}
			case 1:{//carica oss lineare
				visualizzaOsservazioniLineariDisponibili();
				String filepath = InputDati.leggiStringa(Stringhe.INSERISCI_SESSIONE);
				filepath = Stringhe.SAVE_FOLDER+filepath;
				if ( ! filepath.contains(Stringhe.ESTENSIONE_OSS_LIN) ){
					System.out.println(String.format(Stringhe.ESTENSIONE_NON_VALIDA, Stringhe.ESTENSIONE_OSS_LIN));
					break;
				}
				File folder = new File(Stringhe.SAVES_PATH);
				File[] files = folder.listFiles();
				for (File file : files) {

					if (file.getPath().equals(filepath)){
						GestoreFile gf = new GestoreFile();
						try {
							System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
							osservazioneLineare = gf.caricaOsservazioneLineare(filepath);
							System.out.println(String.format(Stringhe.CARICAMENTO_OSS_LIN, osservazioneLineare));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						break;
					}
				}
				break;


			}
			case 2:{//carica automa oss
				visualizzaAutomiOsservazioneDisponibili();
				String filepath = InputDati.leggiStringa(Stringhe.INSERISCI_SESSIONE);
				filepath = Stringhe.SAVE_FOLDER + filepath;
				if ( ! filepath.contains(Stringhe.ESTENSIONE_AUTOMA_OSS) ){
					System.out.println(String.format(Stringhe.ESTENSIONE_NON_VALIDA, Stringhe.ESTENSIONE_AUTOMA_OSS));
					break;
				}
				File folder = new File(Stringhe.SAVES_PATH);
				File[] files = folder.listFiles();
				for (File file : files) {
					if (file.getPath().equals(filepath)){
						GestoreFile gf = new GestoreFile();
						try {
							System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
							oss = gf.caricaAutomaOss(filepath);
							System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, osservazioneLineare));
						} catch (ClassNotFoundException e){
							e.printStackTrace();
						}  catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}

				break;


			}
		}
	}

	private static void gestisciEstensione(int sceltaEstensione) {
		switch (sceltaEstensione){
			case 0:{//back
				break;
			}
			case 1:{//inserisci osservazione
				String filepath = inputNomeFileJSON();
				GestoreFile gf = new GestoreFile();
				gf.setPathOss(filepath);
				try {
					oss = gf.caricaOsservazione();
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, oss.getNome()));
					System.out.println(oss.toStringOss());
					GestoreDizionari gd = new GestoreDizionari();
					diz = gd.estensioneDizionario(diz, ra, oss);
					//TODO

				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_FILEPATH);
				}
				break;
			}


		}
	}

	private static void gestisciMonitoraggio(int sceltaMonitoraggio) {
		switch (sceltaMonitoraggio){
			case 0:{//back
				break;
			}
			case 1:{ //monitoraggio con oss lineare da tastiera

				if ( isNull(sr)){
					System.out.println(Stringhe.NESSUNO_SPAZIO_RILEVANZA);
				}
				String input= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
				osservazioneLineare = new ArrayList<>(Arrays.asList(input.split(", ")));
				GestoreDizionari gd = new GestoreDizionari();
				try {
					gd.effettuaMonitoraggioRevisione(osservazioneLineare, diz, sr);
					for (Terna terna : diz.getTerne()) {
						if (diz.getTerne().size() > 1) System.out.println("Terna " + terna);
					}
				} catch (Exception e) {
					if (diz.getTerne().isEmpty()) System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				break;
			}

			case 2: {//vedi risultato precedente
				if ( isNull(diz.getTerne())){
					System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				else {
					for (Terna terna : diz.getTerne()) {
						if (diz.getTerne().size() > 1) System.out.println("Terna " + terna);
					}
				}
				break;
			}
		}
	}

	private static void gestisciInfoDizionario(int sceltaInfoDiz) {
		//toString ridenominato, toString per info generiche
		switch (sceltaInfoDiz){
			case 0:{//back
				break;
			}
			case 1:{//info generiche
				System.out.println(diz);
				break;
			}
			case 2:{ //info ridenominazione
				System.out.println(diz.toStringRidenominato());
				break;
			}
			case 3:{ //info I/O
				System.out.println(Stringhe.INPUT_OUTPUT);
				for(StatoDizionario s : diz.getMappaDizionario().keySet()) {
					System.out.println(String.format(Stringhe.INFO_I_O, s.getRidenominazione(), s.getInputToString(), s.getOutputToString()));
				}
				System.out.println(Stringhe.COPPIE_INPUT_OUTPUT);
				for(StatoDizionario s : diz.getMappaDizionario().keySet()) {
					System.out.println(String.format(Stringhe.INFO_COPPIE_I_O, s.getRidenominazione(), s.getIOtoString()));
				}
			}
		}
	}

	private static void gestisciInfoSpazioRilevanza(int sceltaInfoSpazioR) {
		switch (sceltaInfoSpazioR){
			case 0: {//back
				break;
			}
			case 1: {//dettagli stati
				System.out.println(String.format(Stringhe.INFO_SPAZIO_RILEVANZA, ""+sr.getStatiRilevanza().size()));
				System.out.println(sr);
			}
			case 2: {//ridenominazione
				for (StatoRilevanzaRete statoRilevanzaRete : sr.getStatiRilevanza()) {
					System.out.println(statoRilevanzaRete + " --> " + statoRilevanzaRete.getRidenominazione());
				}
			}
			case 3:{//mappa spazio
				System.out.println(Stringhe.INFO_MAPPA_SPAZIO);
				for(StatoRilevanzaRete stato : sr.getStatiRilevanza()) {
					for(Pair<Transizione, StatoRilevanzaRete> srd : sr.getMappaStatoRilevanzaTransizioni().get(stato)) {
						System.out.println(stato.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione());
					}
				}
			}
		}
	}

	private static void gestioneCaricamentoDizionario(int sceltaCaricamentoDizionario) throws Exception{
		switch (sceltaCaricamentoDizionario){
			case 0: { //exit
				break;
			}
			case 1: {// carica dizionario
				String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
				File folder = new File(Stringhe.SAVES_PATH);
				File[] files = folder.listFiles();
				for (File file : files) {
					if (file.getName().equals(filepath)){
						GestoreFile gf = new GestoreFile();
						gf.setPathDiz(filepath);
						diz = gf.caricaDizionario();
						break;
					}
					else System.out.println(Stringhe.ERRORE_FILEPATH);
				}
			}
		}
	}

	private static void gestisciCaricamentoReteAutomi() throws Exception{
		String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
		//string file path dovrebbe essere input da controllare e poi prendere indice
		if (!filepath.contains(Stringhe.ESTENSIONE_RETE)){
			System.out.println(String.format(Stringhe.ESTENSIONE_NON_VALIDA, Stringhe.ESTENSIONE_RETE));
			throw new Exception();
		}
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.getName().equals(filepath)){
				GestoreFile gf = new GestoreFile();
				gf.setPathRete(filepath);
				ra = gf.caricaRete();
				break;
			}
		}
		System.out.println(Stringhe.ERRORE_FILEPATH);
		throw new Exception();
	}

	private static Dizionario calcolaDizionario(int dimensione) {
		GestoreDizionari gd = new GestoreDizionari();
		InputOutput input = new InputOutput();
		input.setRete(ra);
		input.setDistanzaMax(dimensione);
		if ( ! spazioRilevanzaCalcolato){
			sr = gd.calcolaSpazioRilevanza(input);
			input.setSr(sr);
		}
		else {
			input.setSr(sr);
		}
		return gd.calcolaDizionario(input);
	}

	private static Dizionario calcolaDizionarioParzialeDaOsservazione() {
		GestoreDizionari gd = new GestoreDizionari();
		InputOutput input = new InputOutput();
		input.setRete(ra);
		input.setOsservazione(oss);
		input.setDaOsservazione(true);
		if ( ! spazioRilevanzaCalcolato ){
			sr = gd.calcolaSpazioRilevanza(input);
			input.setSr(sr);
		}
		else {
			input.setSr(sr);
		}
		return gd.calcolaDizionario(input);
	}

	private static void effettuaRicerca(ArrayList<String> osservazioneLineare) {
		GestoreDizionari gd = new GestoreDizionari();
		try {
			InputOutput inputOutput = new InputOutput();
			inputOutput.setOsservazioneLineare(osservazioneLineare);
			decorazione = gd.effettuaRicerca(inputOutput, diz);
			System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineare, decorazione));

		} catch (Exception e) {
			System.out.println(Stringhe.NESSUN_RISULTATO);
		}
	}

	private static String inputNomeFileJSON(){
		String fileJSON = leggiStringa(Stringhe.INSERIRE_PERCORSO_FILE);
		return fileJSON;
	}

	private static boolean sessioniDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		boolean flag = false;
		if (files.length > 0) flag = true;
		return flag;

	}

	private static String creaNomeFile() {
		Date ora = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		return formatter.format(ora).trim().replace(' ', '_');
	}

	private static void stampaFileDiEsempio() {
		File folder = new File(Stringhe.EXAMPLE_PATH);
		filesEsempio = folder.listFiles();
		int index = 0;
		for (File file : filesEsempio) {
			System.out.println(index + " " + file.getPath());
			index++;
		}
	}

	private static void visualizzaSessioniDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		filesSessione = folder.listFiles();
		int index = 0;
		for (File file : filesSessione) {
			System.out.println(index + " " + file.getName());
			index++;
		}
	}

	public static void visualizzaOsservazioniLineariDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.getName().contains(Stringhe.ESTENSIONE_OSS_LIN)) System.out.println(file.getName());
		}
	}

	private static void visualizzaAutomiOsservazioneDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.getName().contains(Stringhe.ESTENSIONE_AUTOMA_OSS)) System.out.println(file.getName());
		}
	}

	private static void visualizzaSpaziRilevanzaDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.getName().contains(Stringhe.ESTENSIONE_SPAZIO)) System.out.println(file.getName());
		}
	}

	private static boolean rispostaNegativa(String risposta){
		if (risposta.equalsIgnoreCase("n") || risposta.equalsIgnoreCase("no"))
			return true;
		return false;
	}

	private static boolean rispostaValida(String risposta){
		for (String s : Stringhe.RISPOSTE_VALIDE) {
			if (risposta.equalsIgnoreCase(s))return true;
		}
		return false;
	}

	private static String determinaFilepath(String inputUtente) {
		if (VerificaDati.isCifraSingola(inputUtente)){
			return filesEsempio[Integer.parseInt(inputUtente)].getPath();
		}
		return inputUtente;
	}
}
