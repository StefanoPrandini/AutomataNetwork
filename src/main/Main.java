package main;

import algoritmo.Dizionario;
import algoritmo.SpazioRilevanza;
import gestore.GestoreDizionari;
import gestore.GestoreFile;
import gestore.GestoreInputOutput;
import javafx.util.Pair;
import myLib.*;
import model.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.isNull;
import static myLib.Utility.*;

public class Main {

	private static ReteAutomi ra;
	private static Automa automaOss;
	private static SpazioRilevanza sr;
	private static Dizionario diz;
	private static ArrayList<String> osservazioneLineareRicerca = new ArrayList<>();
	private static ArrayList<String> osservazioneLineareMonitoraggio = new ArrayList<>();
	private static Set<Set<String>> decorazione;
	private static boolean spazioRilevanzaCalcolato = false;
	private static ArrayList<File> filesSessione = new ArrayList<>();
	private static ArrayList<File> filesEsempio = new ArrayList<>();
	private static ArrayList<File> sessioniIntere = new ArrayList<>();



	public static void main(String[] args) {
		MyMenu m = new MyMenu(Stringhe.TITOLO_INIZIALE, Stringhe.OPZIONI_MENU_CARICAMENTO, true);
		int scelta = m.scegli();
		while (scelta !=0) {
			gestisciCaricamentoIniziale(scelta);
			scelta = m.scegli();
		}
	}

	private static void gestisciCaricamentoIniziale(int scelta) {

		switch (scelta){
			case 0: break; //EXIT

			case 1:  { //carica da JSON
				stampaFileDiEsempio();
				String input = determinaFilepathEsempio(inputNomeFileJSON());
				if (isNull(input)){
					break;
				}
				GestoreFile gf = new GestoreFile();
				try {
					ra = gf.caricaReteDaJSON(input);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, ra.getNome()));
					System.out.println(ra.toString());
					MyMenu menuGestioneRete = new MyMenu(Stringhe.TITOLO_GESTIONE_RETE, Stringhe.OPZIONI_GESTIONE_RETE);
					int sceltaGestioneRete = menuGestioneRete.scegli();
					while (sceltaGestioneRete != 0){
						gestisciRete(sceltaGestioneRete);
						sceltaGestioneRete = menuGestioneRete.scegli();
					}
				}
//				catch (NullPointerException npe){
//					System.out.println(Stringhe.ERRORE_CARICAMENTO);
//				}
				catch (Exception e){
					System.out.println(Stringhe.ERRORE_CARICAMENTO);
				}

				break;

			}
			case 2: { // carica da sessione
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_RETE);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_RETE, filepath);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, ra.getNome()));
					System.out.println("\n" + ra);
					MyMenu menuGestioneRete = new MyMenu(Stringhe.TITOLO_GESTIONE_RETE, Stringhe.OPZIONI_GESTIONE_RETE);
					int sceltaGestioneRete = menuGestioneRete.scegli();
					while (sceltaGestioneRete != 0){
						gestisciRete(sceltaGestioneRete);
						sceltaGestioneRete = menuGestioneRete.scegli();
					}
				} catch (Exception e){
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}

				break;
			}

			case 3: { //carica intera sessione
				String sessione = inputFilepathSessioneIntera();
				if (isNull(sessione)) break;
				else {
					if (caricaTuttiOggettiInCartella(sessione + File.separator)) {
						MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
						int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
						while (sceltaGestioneDizionario != 0) {
							gestisciDizionario(sceltaGestioneDizionario);
							sceltaGestioneDizionario = menuGestioneDizionario.scegli();
						}
					}
					else System.out.println(Stringhe.ERRORE_CARICAMENTO);
				}
				break;
			}
		}
	}

	private static void gestisciRete(int scelta){
		//informazioni rete, calcola dizionario, carica dizionario
		switch (scelta){
			case 0: { //back
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
					spazioRilevanzaCalcolato = false;
					gestisciCalcoloDizionario(sceltaCalcoloDizionario);
					sceltaCalcoloDizionario = menuCalcolaDizionario.scegli();
				}
				break;
			}
			case 3:{//carica dizionario
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_DIZ);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_DIZ, filepath);
					System.out.println(Stringhe.CARICAMENTO_RIUSCITO);
					MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
					int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					while (sceltaGestioneDizionario != 0){
						gestisciDizionario(sceltaGestioneDizionario);
						sceltaGestioneDizionario = menuGestioneDizionario.scegli();
					}
				} catch (Exception e) {
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}
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
				if (gestioneCaricamentoSpazioRilevanzaDaSerializzazione()){
					System.out.println(Stringhe.CONTINUA_CALCOLO_DIZ);
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
				diz = calcolaDizionario(lettura);
				MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
				int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				while (sceltaGestioneDizionario != 0){
					gestisciDizionario(sceltaGestioneDizionario);
					sceltaGestioneDizionario = menuGestioneDizionario.scegli();
				}
				break;
			}

			case 2: { // da osservazione

				MyMenu menuSceltaCaricamentoJSONoSessione = new MyMenu(Stringhe.TITOLO_JSON_O_SESSIONE, Stringhe.OPZIONI_JSON_O_SESSIONE);
				int sceltaCaricamentoJSONSess = menuSceltaCaricamentoJSONoSessione.scegli();
				while ( sceltaCaricamentoJSONSess != 0){
					if (gestisciCaricamentoAutomaOss(sceltaCaricamentoJSONSess)){
						diz = calcolaDizionarioParzialeDaOsservazione();
						MyMenu menuGestioneDizionario = new MyMenu(Stringhe.TITOLO_GESTIONE_DIZIONARIO, Stringhe.OPZIONI_GESTIONE_DIZIONARIO);
						int sceltaGestioneDizionario = menuGestioneDizionario.scegli();
						while (sceltaGestioneDizionario != 0){
							gestisciDizionario(sceltaGestioneDizionario);
							sceltaGestioneDizionario = menuGestioneDizionario.scegli();
						}
					}
					sceltaCaricamentoJSONSess = menuSceltaCaricamentoJSONoSessione.scegli();
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
					break;
					//TODO check con Ste se estensione funziona
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
				case 7:{ //gestione salvataggio
					MyMenu menuGestioneSalvataggi = new MyMenu(Stringhe.TITOLO_GESTIONE_SALVATAGGI, Stringhe.OPZIONI_GESTIONE_SALVATAGGI);
					int sceltaGestioneSalvataggi = menuGestioneSalvataggi.scegli();
					while (sceltaGestioneSalvataggi != 0){
						gestisciFileSalvataggio(sceltaGestioneSalvataggi);
						sceltaGestioneSalvataggi = menuGestioneSalvataggi.scegli();
					}
					break;
				}

				case 8: { //chiudi elaboratore
					String vuoiUscire = InputDati.leggiStringa(Stringhe.VUOI_USCIRE);
					while ( ! rispostaValida(vuoiUscire) ){
						System.out.println(Stringhe.NON_VALIDA);
						vuoiUscire = InputDati.leggiStringa(Stringhe.VUOI_USCIRE);
					}
					if (rispondeNo(vuoiUscire)){
						break;
					}
					else System.exit(0);

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

	private static void gestisciRicerca(int sceltaRicerca) {
		switch (sceltaRicerca){
			case 0:{//back
				break;
			}

			case 1: {//effettua ricerca
				if (isNull(osservazioneLineareRicerca) || osservazioneLineareRicerca.isEmpty()){
					System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.RICERCA_DIAGNOSI));
					break;
				}
				effettuaRicerca(osservazioneLineareRicerca);
				break;
			}

			case 2:{ //oss lineare da tastiera
				ArrayList<String> input = inserimentoOsservazioneLineare(Stringhe.INSERIMENTO_OSSERVAZIONE);
				if (isNull(input)) break;
				osservazioneLineareRicerca = input;
				effettuaRicerca(osservazioneLineareRicerca);
				break;
			}

			case 3: {//estendi ricerca precedente
				if (isNull(osservazioneLineareRicerca) || osservazioneLineareRicerca.isEmpty()){
					System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.RICERCA_DIAGNOSI));
					break;
				}
				System.out.println(String.format(Stringhe.OSS_LIN_IN_MEMORIA, osservazioneLineareRicerca));
				ArrayList<String> input = inserimentoOsservazioneLineare(Stringhe.ESTENSIONE_OSSERVAZIONE);
				if (isNull(input)) break;
				osservazioneLineareRicerca.addAll(input);
				effettuaRicerca(osservazioneLineareRicerca);
				break;
			}

			case 4: {//vedi risultato precedente
				if (isNull(osservazioneLineareRicerca) || osservazioneLineareRicerca.isEmpty()){
					System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.RICERCA_DIAGNOSI));
					break;
				}
				if ( isNull(decorazione) ){
					System.out.println(Stringhe.NESSUN_RISULTATO);
				}
				else System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineareRicerca, decorazione));
				break;
			}
		}
	}

	private static void gestisciMonitoraggio(int sceltaMonitoraggio) {
		switch (sceltaMonitoraggio){
			case 0:{//back
				break;
			}

			case 1: {//effettua monitoraggio
				if (parametriMonitoraggioOk()) effettuaMonitoraggio();
				break;
			}

			case 2:{ //monitoraggio con oss lineare da tastiera
				if (isNull(sr)){
					System.out.println(Stringhe.NESSUNO_SPAZIO_RILEVANZA);
					break;
				}
				ArrayList<String> input = inserimentoOsservazioneLineare(Stringhe.INSERIMENTO_OSSERVAZIONE);
				if (isNull(input)) break;
				osservazioneLineareMonitoraggio = input;
				effettuaMonitoraggio();
				break;
			}

			case 3:{ //estensione osservazione + monitoraggio
				if (parametriMonitoraggioOk()){
					System.out.println(String.format(Stringhe.OSS_LIN_IN_MEMORIA, osservazioneLineareMonitoraggio));
					ArrayList<String> input = inserimentoOsservazioneLineare(Stringhe.ESTENSIONE_OSSERVAZIONE);
					if (isNull(input)) break;
					osservazioneLineareMonitoraggio.addAll(input);
					effettuaMonitoraggio();
				}
				break;
			}

			case 4: {//vedi risultato precedente
				if (parametriMonitoraggioOk()){
					if (isNull(diz.getTerne()) || diz.getTerne().size() <= 1){
						System.out.println(Stringhe.NESSUN_RISULTATO);
					}
					else {
						stampaTerne();
					}
				}
				break;
			}
		}
	}

	private static void gestisciEstensione(int sceltaEstensione) {

		switch (sceltaEstensione){
			case 0:{ //indietro
				break;
			}

			case 1:{ //effettua estensione
				if (isNull(automaOss)){
					System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.ESTENSIONE_DIZIONARIO));
					break;
				}
				effettuaEstensione();
				break;
			}

			case 2:{ //carica nuova osservazione ed effettua estensione
				MyMenu menuSceltaCaricamentoJSONoSessione = new MyMenu(Stringhe.TITOLO_JSON_O_SESSIONE, Stringhe.OPZIONI_JSON_O_SESSIONE);
				int sceltaCaricamentoJSONSess = menuSceltaCaricamentoJSONoSessione.scegli();
				while ( sceltaCaricamentoJSONSess != 0){
					if (gestisciCaricamentoAutomaOss(sceltaCaricamentoJSONSess)){
						effettuaEstensione();
					}
					sceltaCaricamentoJSONSess = menuSceltaCaricamentoJSONoSessione.scegli();
				}
				break;
			}

			case 3:{ //risultato precedente
				if (isNull(automaOss)){
					System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.ESTENSIONE_DIZIONARIO));
					break;
				}
				gestisciInfoDizionario(1);
				break;
			}
		}
	}

	private static boolean gestisciCaricamentoAutomaOss(int sceltaCaricamentoJSONSess) {
		switch (sceltaCaricamentoJSONSess){
			case 0:{ //indietro
				return false;
			}
			case 1:{ //da json
				if (caricaOsservazioneDaJSON()){
					return true;
				}
				break;
			}
			case 2:{ //da sessione
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_AUTOMA_OSS);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_AUTOMA_OSS, filepath);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, automaOss.getNome()));
					if (isNull(automaOss)){
						System.out.println(Stringhe.FILE_VUOTO);
						return false;
					} else {
						System.out.println(automaOss);
						return true;
					}


				} catch (Exception e) {
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}
				break;
			}
		}
		return false;
	}

	private static void gestisciSalvataggio(int sceltaSalva) {
		switch (sceltaSalva){
			case 0: {//back
				break;
			}
			case 1: {//salva rete automi
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_RETE)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}
			case 2: {//salva spazio rilevanza
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_SPAZIO)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}
			case 3: {//salva dizionario
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_DIZ)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}
			case 4: {//salva osservazione lineare per la ricerca
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_OSS_LIN_RIC)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}

			case 5: {//salva osservazione lineare per il monitoraggio
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_OSS_LIN_MON)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}
			case 6:{ //salva automa osservazione
				if ( ! salvaOggetto(Stringhe.SAVES_FOLDER, Stringhe.ESTENSIONE_AUTOMA_OSS)){
					System.out.println(Stringhe.ERRORE_SALVATAGGIO);
				}
				break;
			}
			case 7:{// salva intera sessione
				String input = InputDati.leggiStringa(Stringhe.INSERISCI_NOME);
				if (input.equals(Stringhe.STRINGA_VUOTA)) break;
				try {
					String cartella = creaCartellaSessione(input) + File.separator;
					salvaOggettiSessione(cartella);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private static void gestisciCaricamento(int scelta){

		switch (scelta){
			case 0:{//back
				break;
			}
			case 1:{//carica oss lineare ricerca
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_OSS_LIN_RIC);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_OSS_LIN_RIC, filepath);
					System.out.println(String.format(Stringhe.CARICAMENTO_OSS_LIN, osservazioneLineareRicerca));
				} catch (Exception e) {
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}
				break;
			}

			case 2:{//carica oss lineare monitoraggio
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_OSS_LIN_MON);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_OSS_LIN_MON, filepath);
					System.out.println(String.format(Stringhe.CARICAMENTO_OSS_LIN, osservazioneLineareMonitoraggio));
				} catch (Exception e) {
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}
				break;

			}


			case 3:{//carica automa oss
				try {
					String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_AUTOMA_OSS);
					caricaOggettoSerializzato(Stringhe.ESTENSIONE_AUTOMA_OSS, filepath);
					System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, automaOss.getNome()));
					System.out.println(automaOss);
				} catch (Exception e) {
					if (e instanceof StreamCorruptedException){
						System.out.println(Stringhe.FILE_CORROTTO);
					}
					System.out.println(e.getMessage());
				}
				break;
			}

			case 4: { //carica spazio di rilevanza
				gestioneCaricamentoSpazioRilevanzaDaSerializzazione();
			}
		}
	}

	private static void gestisciFileSalvataggio(int sceltaGestioneSalvataggi) {
		switch (sceltaGestioneSalvataggi){
			case 0:{ //indietro
				break;
			}
			case 1:{ //modifica nome
				if (aggiungiTutteSessioniAFilesSessione()){
					visualizzaSessioniDisponibili();
					int scelta = (InputDati.leggiIntero(Stringhe.INSERISCI_SESSIONE, Stringhe.VALORE_USCITA, filesSessione.size()));
					if (scelta == Stringhe.VALORE_USCITA){
						break;
					}
					if (rinominaFile(filesSessione.get(scelta-1).getPath())){
						System.out.println(Stringhe.RINOMINATO);
					}
				}
				else System.out.println(Stringhe.NESSUN_FILE_ADATTO);
				break;
			}
			case 2:{ //elimina
				if (aggiungiTutteSessioniAFilesSessione()){
					visualizzaSessioniDisponibili();
					int scelta = (InputDati.leggiIntero(Stringhe.INSERISCI_SESSIONE, Stringhe.VALORE_USCITA, filesSessione.size()));
					if (scelta == Stringhe.VALORE_USCITA){
						break;
					}
					if (eliminaFile(filesSessione.get(scelta-1).getPath())){
						System.out.println(Stringhe.ELIMINATO);
					}
				}
				else System.out.println(Stringhe.NESSUN_FILE_ADATTO);
				break;
			}
		}
	}



	/**
	 *
	 *
	 *
	 *
	 *
	 */

	private static void gestisciInfoDizionario(int sceltaInfoDiz) {
		//toString ridenominato, toString per info generiche
		switch (sceltaInfoDiz){
			case 0:{//back
				break;
			}
			case 1:{//info generiche
				int numeroTransizioni = 0;
				for (Set<Pair<String, StatoDizionario>> value : diz.getMappaDizionario().values()) {
					numeroTransizioni += value.size();
				}
				System.out.println(String.format(Stringhe.INFO_DIZIONARIO, diz.getStatiDizionario().size(),numeroTransizioni));
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
				System.out.println(sr);
				int numeroTransizioni = 0;
				for (List<Pair<Transizione, StatoRilevanzaRete>> value : sr.getMappaStatoRilevanzaTransizioni().values()) {
					numeroTransizioni += value.size();
				}
				System.out.println(String.format(Stringhe.INFO_SPAZIO_RILEVANZA, sr.getStatiRilevanza().size(), numeroTransizioni));
				break;

			}
			case 2: {//ridenominazione
				System.out.println(Stringhe.INFO_RIDENOMINAZIONE_SPAZIO);
				for (StatoRilevanzaRete statoRilevanzaRete : sr.getStatiRilevanza()) {
					System.out.println(statoRilevanzaRete.toStringSenzaRidenominazione() + " --> " + statoRilevanzaRete.getRidenominazione());
				}
				break;
			}
			case 3:{//mappa spazio
				System.out.println(Stringhe.INFO_MAPPA_SPAZIO);
				for(StatoRilevanzaRete stato : sr.getStatiRilevanza()) {
					for(Pair<Transizione, StatoRilevanzaRete> srd : sr.getMappaStatoRilevanzaTransizioni().get(stato)) {
						System.out.println("\t" + stato.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione());
					}
				}
				break;
			}
		}
	}

	private static void effettuaRicerca(ArrayList<String> osservazioneLineare) {
		GestoreDizionari gd = new GestoreDizionari();
		GestoreInputOutput inputOutput = new GestoreInputOutput();
		inputOutput.setOsservazioneLineareRicerca(osservazioneLineare);
		gd.effettuaRicerca(inputOutput, diz);
		decorazione = diz.getDiagnosi();
		diz.setRicercaTerminata(false);
		if (isNull(decorazione)){
			System.out.println(Stringhe.NESSUN_RISULTATO);
		} else if (risultatoRicercaVuoto()){
			System.out.println("WIP 753, risultato vuoto" + "\n" + osservazioneLineare + "\n" + decorazione);
		}
		else System.out.println(String.format(Stringhe.RISULTATO_RICERCA, osservazioneLineare, decorazione));
	}

	private static boolean risultatoRicercaVuoto() {
		if (decorazione.size() == 1){
			Iterator<Set<String>> it = decorazione.iterator();
			if (it.hasNext()){
				if (it.next().isEmpty()){
					return true;
				}
			}
		}
		return false;

	}

	private static void effettuaMonitoraggio(){
		GestoreDizionari gd = new GestoreDizionari();
		GestoreInputOutput inputOutput = new GestoreInputOutput();
		inputOutput.setOsservazioneLineareMonitoraggio(osservazioneLineareMonitoraggio);
		inputOutput.setSr(sr);
		inputOutput.inizializzaLogMonitoraggio();
		gd.effettuaMonitoraggioRevisione(inputOutput, diz);
		if (esistonoTerneRisultato()){
			stampaLog(inputOutput);
			stampaTerne();
		} else System.out.println(Stringhe.NESSUN_RISULTATO);
	}

	private static void effettuaEstensione(){
		GestoreDizionari gd = new GestoreDizionari();
		GestoreInputOutput inputOutput  = new GestoreInputOutput();
		inputOutput.setRete(ra);
		inputOutput.setOsservazione(automaOss);
		inputOutput.setDizionario(diz);
		gd.estensioneDizionario(inputOutput);
	}

	private static void stampaFileDiEsempio() {
		File folder = new File(Stringhe.EXAMPLE_PATH);
		filesEsempio = new ArrayList<>();
		filesEsempio.addAll(Arrays.asList(folder.listFiles()));
		int index = 1;
		System.out.println(Stringhe.TITOLO_FILE_ESEMPIO);
		for (File file : filesEsempio) {
			System.out.println(index + " " + file.getPath());
			index++;
		}
		System.out.println("\n" + Stringhe.INDICE_INDIETRO);
	}

	private static String determinaFilepathEsempio(String inputUtente) {
		if (VerificaDati.isCifraSingola(inputUtente)){
			int index = Integer.parseInt(inputUtente);
			if (index == 0){
				return null;
			}
			else if (index > filesEsempio.size()) {
				return inputUtente; // lo prende come path cos� informa che non esiste
			}
			return filesEsempio.get(index-1).getPath();
		}
		return inputUtente;
	}

	public static ArrayList<String> inserimentoOsservazioneLineare(String msg){
		ArrayList<String> res  = new ArrayList<>();
		String input= InputDati.leggiStringa(msg);
		if (input.trim().equals(Stringhe.STRINGA_VUOTA)) return null;
		ArrayList<String> splitted = new ArrayList<>(Arrays.asList(input.split(",")));
		for (String s : splitted) {
			s = s.trim();
			if ( ! s.equals(Stringhe.STRINGA_VUOTA)) res.add(s);
		}
		return res;

	}

	private static void stampaTerne() {
		System.out.println(String.format(Stringhe.RISULTATO_TERNE, osservazioneLineareMonitoraggio));
		for (Terna terna : diz.getTerne()) {
			if (diz.getTerne().size() > 1) System.out.println("\tTerna " + terna);
		}
	}

	private static String inserimentoFileSessione(String estensioneFile){
		if (esistonoSessioniDisponibili(estensioneFile)){
			visualizzaSessioniDisponibili();
			int scelta = (InputDati.leggiIntero(Stringhe.INSERISCI_SESSIONE, Stringhe.VALORE_USCITA, filesSessione.size()));
			if (scelta == Stringhe.VALORE_USCITA){
				return null;
			}
			return filesSessione.get(scelta-1).getPath();
		}
		else{
			System.out.println(Stringhe.NESSUN_FILE_ADATTO);
			return null;
		}
	}

	private static void visualizzaSessioniDisponibili() {
		int index = 0;
		System.out.println(Stringhe.TITOLO_SESSIONI_SALVATE);
		for (File file : filesSessione) {
			index++;
			System.out.println(index  + " " + file.getName());
		}
		System.out.println("\n" + Stringhe.INDICE_INDIETRO);
	}

	private static boolean esistonoSessioniDisponibili(String estensioneFile) {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		boolean flag = false;
		filesSessione = new ArrayList<>();
		for (File file : files) {
			if (file.getPath().contains(estensioneFile)){
				filesSessione.add(file);
			}
		}
		if (filesSessione.size() > 0) flag = true;
		return flag;
	}

	private static boolean aggiungiTutteSessioniAFilesSessione() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		boolean flag = false;
		filesSessione = new ArrayList<>();
		for (File file : files) {
			filesSessione.add(file);
		}
		if (filesSessione.size() > 0) flag = true;
		return flag;
	}

	private static void caricaOggettoSerializzato(String estensioneFile, String filepath) throws Exception{
		if (isNull(filepath)) throw new Exception(Stringhe.CARICAMENTO_ANNULLATO);
		GestoreFile gf = new GestoreFile();
		if (estensioneFile.equals(Stringhe.ESTENSIONE_RETE)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			ra = (ReteAutomi) gf.caricaDaSessione(filepath);
		}
		else if (estensioneFile.equals(Stringhe.ESTENSIONE_SPAZIO)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			sr = (SpazioRilevanza) gf.caricaDaSessione(filepath);
		}
		else if (estensioneFile.equals(Stringhe.ESTENSIONE_OSS_LIN_RIC)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			osservazioneLineareRicerca = (ArrayList<String>) gf.caricaDaSessione(filepath);
		}
		else if (estensioneFile.equals(Stringhe.ESTENSIONE_OSS_LIN_MON)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			osservazioneLineareMonitoraggio = (ArrayList<String>) gf.caricaDaSessione(filepath);
		}
		else if (estensioneFile.equals(Stringhe.ESTENSIONE_DIZ)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			diz = (Dizionario) gf.caricaDaSessione(filepath);
		}
		else if (estensioneFile.equals(Stringhe.ESTENSIONE_AUTOMA_OSS)){
			System.out.println(String.format(Stringhe.CARICAMENTO_IN_CORSO, filepath));
			automaOss = (Automa) gf.caricaDaSessione(filepath);
		}
		else throw new Exception(Stringhe.PROBLEMA_CARICAMENTO_ESTENSIONI);
	}

	private static boolean caricaOsservazioneDaJSON() {
		stampaFileDiEsempio();
		String filepath = determinaFilepathEsempio(inputNomeFileJSON());
		if (isNull(filepath) || filepath.trim().equals(Stringhe.STRINGA_VUOTA)) return false;
		GestoreFile gf = new GestoreFile();
		try {
			automaOss = gf.caricaOsservazioneDaJSON(filepath);
			System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO_CON_NOME, automaOss.getNome()));
			System.out.println(automaOss.toStringOss());
		}
		catch (Exception e){
			if (e instanceof NullPointerException){
				System.out.println(Stringhe.FILE_NON_VALIDO);
			}
			else System.out.println(Stringhe.ERRORE_FILEPATH);
			return false;
		}
		return true;
	}

	/**
	 * metodo necessario solo per gestioneCaricamentoSpazioRilevanza(), di fatto consiste in una sezione di caricaFilesDaSessione
	 * @return
	 * @throws Exception
	 */
	private static SpazioRilevanza caricaSpazio() throws Exception {
		String filepath = inserimentoFileSessione(Stringhe.ESTENSIONE_SPAZIO);
		if (isNull(filepath)) throw new Exception(Stringhe.CARICAMENTO_ANNULLATO);
		GestoreFile gf = new GestoreFile();
		return (SpazioRilevanza) gf.caricaDaSessione(filepath);
	}

	private static boolean controllaNomeRete(SpazioRilevanza spazioRilevanza) {
		boolean sovrascrive = true;
		if ( ! spazioRilevanza.getNomeRete().equals(ra.getNome())){
			System.out.println(Stringhe.NOMI_RETE_DIVERSI);
			System.out.println(String.format(Stringhe.INFO_NOMI_RETE, ra.getNome(), spazioRilevanza.getNomeRete()));
			String vuoiContinuare = InputDati.leggiStringa(Stringhe.SEI_SICURO);
			while ( ! rispostaValida(vuoiContinuare) ){
				System.out.println(Stringhe.NON_VALIDA);
				vuoiContinuare = InputDati.leggiStringa(Stringhe.SEI_SICURO);
			}
			//se vuole inserire uno spazio di rilevanza non relativo alla rete
			if (rispondeNo(vuoiContinuare)){
				sovrascrive = false;
			}
		}
		return sovrascrive;
	}

	private static boolean parametriMonitoraggioOk() {
		if ( isNull(sr)){
			System.out.println(Stringhe.NESSUNO_SPAZIO_RILEVANZA);
			return false;
		}
		if (isNull(osservazioneLineareMonitoraggio) || osservazioneLineareMonitoraggio.isEmpty()){
			System.out.println(String.format(Stringhe.NESSUNA_OSSERVAZIONE_INSERITA, Stringhe.MONITORAGGIO_REVISIONE));
			return false;
		}
		return true;
	}

	private static boolean rinominaFile(String path){
		System.out.println(Stringhe.HAI_SCELTO + path);
		String estensione = Utility.ottieniEstensione(path) + ".ser";
		if (estensione.equals(Stringhe.STRINGA_VUOTA)){
			return false;
		}
		String inputUtente = InputDati.leggiStringa(Stringhe.NUOVO_NOME);
		if (inputUtente.trim().equals(Stringhe.STRINGA_VUOTA)){
			return false;
		}
		File old = new File(path);
		File nuovo = new File(Stringhe.SAVES_FOLDER + inputUtente + estensione);
		return old.renameTo(nuovo);

	}

	private static boolean eliminaFile(String filepath){
		System.out.println(Stringhe.HAI_SCELTO + filepath);
		String vuoiContinuare = InputDati.leggiStringa(Stringhe.ATTENZIONE_ELIMINARE + Stringhe.SEI_SICURO);
		while ( ! rispostaValida(vuoiContinuare) ){
			System.out.println(Stringhe.NON_VALIDA);
			vuoiContinuare = InputDati.leggiStringa(Stringhe.SEI_SICURO);
		}
		if (rispondeNo(vuoiContinuare)){
			return false;
		}
		else {
			File f1 = new File(filepath);
			return f1.delete();
		}
	}

	private static boolean salvaOggetto(String baseFolder, String estensioneFile){
		String nome = baseFolder + dataFormattata() + estensioneFile;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(nome)));
			if (estensioneFile.equals(Stringhe.ESTENSIONE_RETE)){
				oos.writeObject(ra);
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else if (estensioneFile.equals(Stringhe.ESTENSIONE_SPAZIO)){
				oos.writeObject(sr);
				oos.close();
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else if (estensioneFile.equals(Stringhe.ESTENSIONE_OSS_LIN_RIC)){
				oos.writeObject(osservazioneLineareRicerca);
				oos.close();
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else if (estensioneFile.equals(Stringhe.ESTENSIONE_OSS_LIN_MON)){
				oos.writeObject(osservazioneLineareMonitoraggio);
				oos.close();
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else if (estensioneFile.equals(Stringhe.ESTENSIONE_DIZ)){
				oos.writeObject(diz);
				oos.close();
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else if (estensioneFile.equals(Stringhe.ESTENSIONE_AUTOMA_OSS)){
				oos.writeObject(automaOss);
				oos.close();
				System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
			}
			else {
				oos.close();
				return false;
			}
		}
		catch (Exception e){
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	private static String creaCartellaSessione(String input) throws IOException {
		String nomeCartella = input + dataFormattata();
		Path path = Paths.get(Stringhe.SESSIONI_INTERE_FOLDER + nomeCartella + File.separator);
		Files.createDirectory(path);
		return path.toString();
	}

	private static void salvaOggettiSessione(String cartella) {
		salvaOggetto(cartella, Stringhe.ESTENSIONE_RETE);
		salvaOggetto(cartella, Stringhe.ESTENSIONE_SPAZIO);
		salvaOggetto(cartella, Stringhe.ESTENSIONE_DIZ);
		salvaOggetto(cartella, Stringhe.ESTENSIONE_OSS_LIN_RIC);
		salvaOggetto(cartella, Stringhe.ESTENSIONE_OSS_LIN_MON);
		salvaOggetto(cartella, Stringhe.ESTENSIONE_AUTOMA_OSS);
	}

	private static boolean visualizzaCartellaSessioniIntere(){
		File cartella = new File(Stringhe.SESSIONI_INTERE_PATH);
		File[] files = cartella.listFiles();
		sessioniIntere = new ArrayList<>(Arrays.asList(files));
		if (sessioniIntere.isEmpty()) return false;
		int indice = 1;
		System.out.println(Stringhe.TITOLO_SESSIONI_SALVATE);
		for (File file : sessioniIntere) {
			System.out.println(indice +  " " + file.getName());
			indice++;
		}
		System.out.println("\n" + Stringhe.INDICE_INDIETRO);
		return true;
	}

	private static String inputFilepathSessioneIntera() {
		if (visualizzaCartellaSessioniIntere()){
			int scelta = InputDati.leggiIntero(Stringhe.INSERISCI_SESSIONE, Stringhe.VALORE_USCITA, sessioniIntere.size());
			if (scelta == Stringhe.VALORE_USCITA){
				return null;
			}
			return sessioniIntere.get(scelta-1).getPath();
		}
		else {
			System.out.println(Stringhe.NESSUN_FILE_ADATTO);
			return null;
		}
	}

	private static boolean caricaTuttiOggettiInCartella(String filepath) {
		File sessionePath = new File(filepath);
		File[] filesSessione = sessionePath.listFiles();
		for (File oggetto : filesSessione) {
			try {
				caricaOggettoSerializzato(Utility.ottieniEstensione(oggetto.getPath()) + ".ser", oggetto.getPath());
			} catch (Exception e) {
				if (e instanceof StreamCorruptedException){
					System.out.println(Stringhe.FILE_CORROTTO);
				}
				System.out.println(e.getMessage());
				return false;
			}
		}
		System.out.println(Stringhe.SESSIONE_CARICATA);
		return true;

	}

	private static boolean esistonoTerneRisultato() {
		if (diz.getTerne().size() <= 1){
			return false;
		}
		return true;
	}

	private static boolean gestioneCaricamentoSpazioRilevanzaDaSerializzazione() {
		try {
			SpazioRilevanza spazioRilevanza = caricaSpazio();
			boolean vuoleSovrascrivere = controllaNomeRete(spazioRilevanza);
			if (vuoleSovrascrivere){
				sr = spazioRilevanza;
				System.out.println(Stringhe.SPAZIO_CARICATO);
				spazioRilevanzaCalcolato = true;
			}
			else  {
				System.out.println(Stringhe.CARICAMENTO_ANNULLATO);
				spazioRilevanzaCalcolato = false;
			}
			return spazioRilevanzaCalcolato;
		} catch (Exception e) {
			if (e instanceof StreamCorruptedException){
				System.out.println(Stringhe.FILE_CORROTTO);
			}
			System.out.println(e.getMessage());
			spazioRilevanzaCalcolato = false;
			return spazioRilevanzaCalcolato;
		}
	}

	private static Dizionario calcolaDizionario(int dimensione) {
		GestoreDizionari gd = new GestoreDizionari();
		GestoreInputOutput input = new GestoreInputOutput();
		ra.inizializzaRete(); // riporto a stato iniziale
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
		GestoreInputOutput input = new GestoreInputOutput();
		ra.inizializzaRete(); // riporto a stato iniziale
		input.setRete(ra);
		input.setOsservazione(automaOss);
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




}
