package main;

import input.GestoreDizionari;
import input.GestoreFile;
import javafx.util.Pair;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
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
			case 0: break; //EXIT

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
				diz = calcolaDizionarioParzialeDaPrefisso();
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
					MyMenu menuEstensione = new MyMenu(Stringhe.TITOLO_ESTENSIONE, Stringhe.OPZIONI_ESTENSIONE);
					int sceltaEstensione = menuEstensione.scegli();
					while (sceltaEstensione != 0){
						gestisciEstensione(sceltaEstensione);
						sceltaEstensione = menuEstensione.scegli();
					}
					//TODO check con Ste se estensione funziona
					break;
				}
				case 6: {// salva spazio
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
				case 7: { //salva dizionario
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
				case 8: {// salva rete
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
				case 9: { //chiudi elaboratore
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

    private static String creaNomeFile() {
        Date ora = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(ora).trim().replace(' ', '_');
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

	private static Dizionario calcolaDizionarioParzialeDaPrefisso() {
		GestoreDizionari gd = new GestoreDizionari();
		sr = gd.calcolaSpazioRilevanzaPrefisso(ra, lunghezzaPrefisso);
		return gd.calcolaDizionario(sr);
	}

	private static Dizionario calcolaDizionarioParzialeDaOsservazione() {
		GestoreDizionari gd = new GestoreDizionari();
		sr = gd.calcolaSpazioRilevanzaOss(ra, oss);
		return gd.calcolaDizionario(sr);
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

	private static void gestisciRicerca(int sceltaRicerca) {
		switch (sceltaRicerca){
			case 0:{//back
				break;
			}
			case 1:{ //oss lineare da tastiera

				String input= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
				osservazioneLineare = new ArrayList<>(Arrays.asList(input.split(", ")));

				GestoreDizionari gd = new GestoreDizionari();
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

	private static void visualizzaSessioniDisponibili() {
		File folder = new File(Stringhe.SAVES_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
		}
	}

	private static void gestisciCaricamentoReteAutomi() throws Exception{
		String filepath = leggiStringa(Stringhe.INSERISCI_SESSIONE);
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

	private static boolean dizionarioGiaPresente(){
		if (isNull(diz)) return false;
		return true;
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

}
