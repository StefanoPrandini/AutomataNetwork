package main;

import input.GestoreDizionari;
import input.GestoreFile;
import input.InputParser;
import javafx.util.Pair;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
import reteAutomi.Dizionario;
import reteAutomi.ReteAutomi;
import reteAutomi.SpazioRilevanza;
import reteAutomi.Terna;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;
import static myLib.InputDati.leggiStringa;


public class Main {

	private static ReteAutomi ra;
	private static ReteAutomi oss;
	private static SpazioRilevanza sr;
	private static Dizionario diz;
	private static ArrayList<String> osservazioneLineare;
	private static List<Terna> terne;
	private static int lunghezzaPrefisso = SpazioRilevanza.ESPLORAZIONE_COMPLETA;


	public static void main(String[] args) {



		MyMenu m = new MyMenu(Stringhe.TITOLO_INIZIALE, Stringhe.OPZIONI_MENU_CARICAMENTO);

		int scelta = m.scegli();
		while (scelta !=0) {
			gestisciCaricamentoIniziale(scelta);
			scelta = m.scegli();
		}
	}


	private static void gestisciCaricamentoIniziale(int scelta) {

		switch (scelta){
			case 0: break; //EXIT  TODO aggiungere sei sicuro ??

			case 1:  { //carica da JSON
				String filepath = inputNomeFileJSON();
				GestoreFile gf = new GestoreFile();
				gf.setPathRete(filepath);
				try {
					ra = gf.caricaRete();
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO, ra.getNome()));
					MyMenu menuGestioneRete = new MyMenu(Stringhe.TITOLO_GESTIONE_RETE, Stringhe.OPZIONI_GESTIONE_RETE);
					int sceltaGestioneRete = menuGestioneRete.scegli();
					while (sceltaGestioneRete != 0){
						gestisciRete(sceltaGestioneRete);
						sceltaGestioneRete = menuGestioneRete.scegli();
					}

				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_FILEPATH);
				}
				break;

			}
			case 2: { // carica sessione
				if(sessioniDisponibili()){
					visualizzaSessioniDisponibili();
					try {
						gestisciCaricamentoReteAutomi();
						System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO, ra.getNome()));
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
				gestisciDizionario();
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
				diz = calcolaDizionario();
				gestisciDizionario();
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
				lunghezzaPrefisso = lettura;
				diz = calcolaDizionarioParziale(lunghezzaPrefisso);
				gestisciDizionario();
				break;
			}
			case 2: { // da osservazione
				String percorsoOss = InputDati.leggiStringa(Stringhe.INSERISCI_PERCORSO_OSSERVAZIONE);
				if (percorsoOss.equals("" + Stringhe.VALORE_USCITA)) break;
				GestoreFile gf = new GestoreFile();
				try{
					oss = gf.caricaOsservazione(percorsoOss);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO, oss.getNome()));
				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_FILEPATH);
				}
				diz = calcolaDizionarioParziale(oss);
				gestisciDizionario();
				break;
			}
		}
	}




	//nono e decimo, precondizione: il dizionario è stato calcolato
	private static void gestisciDizionario() {
		MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
		int scelta = menuGestioneDizionario.scegli();
		while (scelta != Stringhe.VALORE_USCITA ){
			switch (scelta){

			}
			scelta = menuGestioneDizionario.scegli();
		}
	}

	private static Dizionario calcolaDizionarioParziale(int lunghezzaPrefisso) {
		return null;
	}

	private static Dizionario calcolaDizionarioParziale(ReteAutomi osservazione) {
		return null;
	}

	private static Dizionario calcolaDizionario() {
		return null;
	}


	private static void gestioneCaricamentoDizionario(int sceltaCaricamentoDizionario) throws Exception{
		switch (sceltaCaricamentoDizionario){
			case 0: { //exit
				break;
			}
			case 1: {// carica dizionario
				String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
				File folder = new File(Stringhe.SAVE_FOLDER);
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

	private static void visualizzaSessioniDisponibili() {
		File folder = new File(Stringhe.SAVE_FOLDER);
		File[] files = folder.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}
	}

	private static void gestisciCaricamentoReteAutomi() throws Exception{
		String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
		File folder = new File(Stringhe.SAVE_FOLDER);
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

	private static boolean dizionarioGiaPresente(){
		if (isNull(diz)) return false;
		return true;
	}



	/**
	 *
	 *
	 *
	 *
	 *
	 *			MENU VECCHI, DA TOGLIERE ALLA FINE
	 *
	 *
	 *
	 *
	 */

	//TODO opzioni aggiungibili:
	//spazio di rilevanza
	private static void deprecatedSwitcher(int scelta){
		String filepath;
		GestoreFile gf;
		GestoreDizionari gd;

		switch(scelta){
			case 1: { //carica rete automi

				filepath = inputNomeFileJSON();
				//gf = new GestoreFile(filepath);
				//ra = gf.caricaRete();


				//TODO
				break;
			}
			case 2: { //carica osservazione

				filepath = inputNomeFileJSON();
				//gf = new GestoreFile(filepath);
				//oss = gf.caricaOsservazione();
			}

			case 3: { //Dizionario completo


				MyMenu sottoMenu3 = new MyMenu(Stringhe.TITOLO_SOTTO_MENU_DIZIONARIO_COMPLETO, Stringhe.OPZIONI_SOTTO_MENU_DIZIONARIO_COMPLETO);
				int sceltaSottoMenu3 = sottoMenu3.scegli();
				if (sceltaSottoMenu3 == 0) break;
				if ( ! isNull(ra) ){
					gd = new GestoreDizionari(ra);
					switcherDizionarioCompleto(sceltaSottoMenu3, gd);
				}
				else{
					System.out.println(Stringhe.NESSUNA_RETE_CARICATA);
				}
				break;
			}
			case 4: { //"Monitoraggio",


				MyMenu sottoMenu4 = new MyMenu(Stringhe.TITOLO_SOTTO_MENU_MONITORAGGIO, Stringhe.OPZIONI_SOTTO_MENU_MONITORAGGIO);
				int sceltaMenu4 = sottoMenu4.scegli();
				if (sceltaMenu4 == 0) break;

				switcherMonitoraggio(sceltaMenu4);
				break;
			}
			case 5: { //"Prefisso",

				MyMenu sottoMenu5 = new MyMenu(Stringhe.TITOLO_MENU_PREFISSO, Stringhe.OPZIONI_MENU_PREFISSO);
				int scelta5 = sottoMenu5.scegli();
				if (scelta5 == 0) break;
				switcherPrefisso(scelta5);
				break;
			}
			case 6: { //"Dizionari parziali",


			}
			case 7: { //"Estensione dizionario"

				filepath = inputNomeFileJSON();
			}
			case 8: { //"Riassunto automi"

				filepath = inputNomeFileJSON();
			}
			case 9: { //salva sessione

			}
			case 10: { //carica sessione

				if(sessioniDisponibili()){
					MyMenu sottoMenu10 = new MyMenu(Stringhe.TITOLO_MENU_10, Stringhe.OPZIONI_MENU_10);
					int scelta10 = sottoMenu10.scegli();
					if (scelta == 0) break;
					switcherCaricaSessione(scelta10);
				}
				break;
			}



			default: break;
		}


	}

	private static void switcherPrefisso(int scelta5) {
		switch (scelta5){
			case 1:{
				//inserisci prefisso
				lunghezzaPrefisso = InputDati.leggiIntero(Stringhe.LEGGI_INTERO);
				System.out.println(Stringhe.SALVATO);
				break;
			}
			case 2: {
				//calcolca prefisso dizionario
				if (reteAutomiCaricata()){
					//TODO secondo inserimento del prefisso funziona ma non fa andare
					GestoreDizionari gd = new GestoreDizionari(ra);
					Pair res = gd.costruisciPrefisso(lunghezzaPrefisso);
					System.out.println(res);
					sr = (SpazioRilevanza)res.getKey();
					diz = (Dizionario)res.getValue();
					String distSR = "Spazio di rilevanza con distanza " +  lunghezzaPrefisso + ": ";
					String distDiz = "Prefisso del dizionario di lunghezza " + lunghezzaPrefisso + ": ";
					if (lunghezzaPrefisso == SpazioRilevanza.ESPLORAZIONE_COMPLETA){
						distSR = "Spazio di rilevanza completo";
						distDiz = "Dizionario completo";
					}
					System.out.println(distSR);
					System.out.println(gd.infoSpazioRilevanza(sr));
					System.out.println(distDiz);
					System.out.println(gd.infoDizionario(diz));



			}

				break;
			}
		}
	}

	private static void switcherCaricaSessione(int scelta10) {
		String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
		File folder = new File(Stringhe.SAVE_FOLDER);
		File[] files = folder.listFiles();
		File sessione = null;
		boolean flag = false;
		for (File file : files) {
			if (file.toString().equals(filepath)){
				sessione = file;
				flag = true;
				break;
			}
		}
		if ( ! flag){
			System.out.println("Nessuna sessione presente con il nome specificato");
		}else {
			if (scelta10 == 1){
				System.out.println("Carico sessione");
				caricaSessione(sessione);
				System.out.println("Sessione caricata");
			}
			else if (scelta10 == 2){
				System.out.println("Elimino sessione");
				eliminaSessione(filepath);
				System.out.println("Sessione eliminata");
				
			}
		}
		
		
	}

	private static void eliminaSessione(String filepath) {
	}

	private static void caricaSessione(File sessione) {
	}

	private static boolean sessioniDisponibili() {
		File folder = new File(Stringhe.SAVE_FOLDER);
		File[] files = folder.listFiles();
		boolean flag = false;
		if (files.length > 0) flag = true;
		return flag;

	}

	private static void switcherMonitoraggio(int sceltaMenu4) {
		switch (sceltaMenu4){
			case 1: {
				//inserisci osservazione lineare

				osservazioneLineare = inserimentoOsservazioneLineare();
				System.out.println("Sono state inserite queste etichette: ");
				for (String s : osservazioneLineare) {
					System.out.println(s);
				}
				break;
			}
			case 2: {
				//TODO effettua monitoraggio
				if (isNull(osservazioneLineare) || osservazioneLineare.equals("")){
					System.out.println(Stringhe.NESSUNA_OSSERVAZIONE);
				}
				else {
					GestoreDizionari gd = new GestoreDizionari(ra);
					terne = gd.effettuaMonitoraggio(diz, osservazioneLineare, sr);
					if (terne.size() > 0){
						System.out.println("Risultato: ");
						for (Terna terna : terne) {
							System.out.println("Terna " + terna);
						}
					}
				}
				break;
			}
			default: break;
		}

	}

	private static ArrayList<String> inserimentoOsservazioneLineare() {
		String input = InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
		return new ArrayList<>(Arrays.asList(input.split(", ")));
	}

	private static void switcherDizionarioCompleto(int sceltaSottoMenu3, GestoreDizionari gestoreDizionari) {
		switch (sceltaSottoMenu3){
			case 1: {
				costruisciDizionarioCompleto(gestoreDizionari);
				break;
			}
			case 2: {
				//TODO
				if ( !isNull(diz)) System.out.println(gestoreDizionari.infoDizionario(diz));
				else System.out.println(Stringhe.NESSUN_DIZIONARIO);
				break;
			}
			case 3: {
				//TODO
				if ( !isNull(sr)) System.out.println(gestoreDizionari.infoSpazioRilevanza(sr));
				else System.out.println(Stringhe.NESSUN_S_R);
				break;
			}
			default: break;
		}
	}

	private static String inputNomeFileJSON(){
		String fileJSON = leggiStringa(Stringhe.INSERIRE_PERCORSO_FILE);
		return fileJSON;
	}

	private static boolean reteAutomiCaricata(){
		return !isNull(ra);
	}

	private static void costruisciDizionarioCompleto(GestoreDizionari gestoreDizionari){
		if (reteAutomiCaricata()){
			gestoreDizionari = new GestoreDizionari(ra);
			System.out.println(Stringhe.COSTRUZIONE_S_R);
			sr = gestoreDizionari.costruisciSpazioRilevanza();
			System.out.println(sr);
			System.out.println(Stringhe.FINITO);
			System.out.println(Stringhe.COSTRUZIONE_DIZIONARIO);
			diz = gestoreDizionari.costruisciDizionarioCompleto(sr);
			System.out.println(diz);
			System.out.println(Stringhe.FINITO);
		}
		else{
			System.out.println(Stringhe.NESSUNA_RETE_CARICATA);
		}
	}



}
