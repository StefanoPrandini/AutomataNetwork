package main;

import input.GestoreDizionari;
import input.GestoreFile;
import javafx.util.Pair;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
import reteAutomi.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static java.util.Objects.isNull;
import static myLib.InputDati.leggiStringa;


public class Main {

	private static ReteAutomi ra;
	private static ReteAutomi oss;
	private static SpazioRilevanza sr;
	private static Dizionario diz;
	private static ArrayList<String> osservazioneLineare;
	private static Set<Set<String>> decorazione;
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
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, ra.getNome()));
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
				diz = calcolaDizionarioCompleto();
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
				System.out.println("WIP"); //TODO
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
				try{
					oss = gf.caricaOsservazione(percorsoOss);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, oss.getNome()));
					diz = calcolaDizionarioParziale(oss);
					MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
					int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					while (sceltaGestioneDizionario != 0){
						gestisciDizionario(sceltaGestioneDizionario);
						sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					}
				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_CARICAMENTO);
				}

				break;
			}
		}
	}




	//nono e decimo, precondizione: il dizionario è stato calcolato
	private static void gestisciDizionario(int scelta) {
			switch (scelta) {
				case 0: { //back
					System.out.println(scelta);

					break;
				}
				case 1: { //info spazio rilevanza
					MyMenu menuInfoSpazioRilevanza = new MyMenu(Stringhe.TITOLO_INFO_SPAZIO_R, Stringhe.OPZIONI_INFO_SPAZIO_R);
					int sceltaInfoSpazioR = menuInfoSpazioRilevanza.scegli();
					while (sceltaInfoSpazioR != 0){
						gestisciInfoSpazioRilevanza(sceltaInfoSpazioR);
						sceltaInfoSpazioR = menuInfoSpazioRilevanza.scegli();
					}
					break;
				}
				case 2: {//info dizionario
					MyMenu menuInfoDizionario = new MyMenu(Stringhe.TITOLO_INFO_DIZIONARIO, Stringhe.OPZIONI_INFO_DIZIONARIO);
					int sceltaInfoDiz = menuInfoDizionario.scegli();
					while (sceltaInfoDiz != 0){
						gestisciInfoDizionario(sceltaInfoDiz);
						sceltaInfoDiz = menuInfoDizionario.scegli();
					}
					break;
				}
				case 3: {//ricerca in dizionario
					MyMenu menuRicercaDizionario = new MyMenu(Stringhe.TITOLO_RICERCA_DIZIONARIO, Stringhe.OPZIONI_RICERCA_DIZIONARIO);
					int sceltaRicerca = menuRicercaDizionario.scegli();
					while (sceltaRicerca != 0){
						gestisciRicerca(sceltaRicerca);
						sceltaRicerca = menuRicercaDizionario.scegli();
					}
					break;
				}
				case 4: {//monitoraggio e revisione
					MyMenu menuMonitoraggioRevisione = new MyMenu(Stringhe.TITOLO_MONITORAGGIO, Stringhe.OPZIONI_MONITORAGGIO);
					int sceltaMonitoraggio = menuMonitoraggioRevisione.scegli();
					while (sceltaMonitoraggio != 0){
						gestisciMonitoraggio(sceltaMonitoraggio);
						sceltaMonitoraggio = menuMonitoraggioRevisione.scegli();
					}
					break;
				}
				case 5: {//estensione

					break;
				}
				case 6: {// salva spazio

					break;
				}
				case 7: { //salva dizionario

					break;
				}
			}
	}




	private static Dizionario calcolaDizionarioParziale(int lunghezzaPrefisso) {
		//TODO
		return null;
	}

	private static Dizionario calcolaDizionarioParziale(ReteAutomi osservazione) {
		//TODO
		return null;
	}

	private static Dizionario calcolaDizionarioCompleto() {
		GestoreDizionari gd = new GestoreDizionari();
		sr = gd.calcolaSpazioRilevanzaPrefisso(ra, SpazioRilevanza.ESPLORAZIONE_COMPLETA);
		return gd.calcolaDizionario(sr);
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
				GestoreDizionari gd = new GestoreDizionari(null);
				try {
					gd.effettuaMonitoraggioRevisione(osservazioneLineare, diz, sr);
					System.out.println(Stringhe.RISULTATO_MONITORAGGIO);
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
					System.out.println(Stringhe.RISULTATO_MONITORAGGIO);
					for (Terna terna : diz.getTerne()) {
						if (diz.getTerne().size() > 1) System.out.println("Terna " + terna);
					}
				}
				break;
			}
		}
	}

	private static void gestisciRicerca(int sceltaRicerca) {
		switch (sceltaRicerca){
			case 0:{//back
				break;
			}
			case 1:{ //oss lineare da tastiera

				String input= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
				osservazioneLineare = new ArrayList<>(Arrays.asList(input.split(", ")));

				GestoreDizionari gd = new GestoreDizionari(null);
				try {
					decorazione = gd.effettuaRicerca(osservazioneLineare, diz);
					System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineare, decorazione));

				} catch (Exception e) {
					System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				break;
			}

			case 2: {//vedi risultato precedente
				if ( isNull(decorazione)){
					System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				else System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineare, decorazione));
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
				/**

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
				}**/
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
