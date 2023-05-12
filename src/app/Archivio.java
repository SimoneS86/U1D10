package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import entità.Libro;
import entità.OggettiLeggibili;
import entità.Rivista;

public class Archivio {
	private static final Logger logger = LoggerFactory.getLogger(Archivio.class);
	private Map<UUID, OggettiLeggibili> archivio;

	public Archivio() {
		this.archivio = new HashMap<UUID, OggettiLeggibili>();
	}

	public void aggiungiElemento(OggettiLeggibili elemento) {
		archivio.put(elemento.getIsbn(), elemento);
	}

	public void rimuoviPerIsbn(UUID isbn) {
		OggettiLeggibili elementoRimosso = archivio.remove(isbn);
		if (elementoRimosso != null)
			logger.info("Elemento rimosso dall'archivio:" + "\n" + elementoRimosso.toString() + "\n");

	}

	public OggettiLeggibili cercaPerIsbn(UUID isbn) {
		return archivio.get(isbn);
	}

	public List<OggettiLeggibili> cercaPerAnno(int anno) {
		return archivio.values().stream().filter(obj -> obj.getAnnoPubblicazione() == anno).toList();
	}

	public List<Libro> cercaPerAutore(String autore) {
		return archivio.values().stream().filter(elem -> elem instanceof Libro).map(elem -> (Libro) elem)
				.filter(elem -> autore.equals(elem.getAutore())).toList();
	}

	public void salvaSuFile() throws IOException {
		File storeFile = new File("archivio.txt");
		List<String> oggettiLeggibiliToString = new ArrayList<>();
		for (OggettiLeggibili obj : archivio.values()) {
			if (obj instanceof Libro) {
				Libro l = (Libro) obj;
				oggettiLeggibiliToString.add(l.getIsbn() + "@" + l.getTitolo() + "@" + l.getAnnoPubblicazione() + "@"
						+ l.getNumeroPagine() + "@" + l.getAutore() + "@" + l.getGenere() + "@");
			} else if (obj instanceof Rivista) {
				Rivista r = (Rivista) obj;
				oggettiLeggibiliToString.add(r.getIsbn() + "@" + r.getTitolo() + "@" + r.getAnnoPubblicazione() + "@"
						+ r.getNumeroPagine() + "@" + r.getPeriodicita());
			} else {
				throw new IllegalArgumentException("oggetto non leggibile");
			}
		}
		String data = String.join("#", oggettiLeggibiliToString);
		FileUtils.writeStringToFile(storeFile, data, "UTF-8", true);
	}

	public void caricaDaFile() throws IOException {
		File storeFile = new File("archivio.txt");
		String data = FileUtils.readFileToString(storeFile, "UTF-8");
		String[] oggettiLeggibiliToString = data.split("#");

		for (String str : oggettiLeggibiliToString) {
			String[] objAttributi = str.split("@");

			if (objAttributi.length == 6) {
				UUID isbn = UUID.fromString(objAttributi[0]);
				String titolo = objAttributi[1];
				int annoPubblicazione = Integer.parseInt(objAttributi[2]);
				int numeroPagine = Integer.parseInt(objAttributi[3]);
				String autore = objAttributi[4];
				String genere = objAttributi[5];

				OggettiLeggibili obj = new Libro(isbn, titolo, autore, genere, annoPubblicazione, numeroPagine);
				archivio.put(isbn, obj);
			} else if (objAttributi.length == 5) {
				UUID isbn = UUID.fromString(objAttributi[0]);
				String titolo = objAttributi[1];
				int annoPubblicazione = Integer.parseInt(objAttributi[2]);
				int numeroPagine = Integer.parseInt(objAttributi[3]);
				Rivista.Periodicita periodicita = Rivista.Periodicita.valueOf(objAttributi[4]);

				OggettiLeggibili obj = new Rivista(isbn, titolo, annoPubblicazione, numeroPagine, periodicita);
				archivio.put(isbn, obj);
			} else {
				throw new IllegalArgumentException("Formato oggetto non valido.");
			}
		}
		new FileWriter(storeFile).close();
	}

	@Override
	public String toString() {
		String result = "Archivio:\n";
		for (OggettiLeggibili elemento : archivio.values()) {
			result += "- " + elemento + "\n";
		}
		return result;
	}

	public static void main(String[] args) {
		Archivio archivio = new Archivio();

		// AGGIUNTA DI UN ELEMENTO
		logger.info("AGGIUNGI ELEMENTO");
		Libro libro1 = new Libro(UUID.randomUUID(), "Il nome della rosa", "Umberto Eco", "Giallo storico", 1980, 600);
		archivio.aggiungiElemento(libro1);

		Rivista rivista1 = new Rivista(UUID.randomUUID(), "Wired Italia", 2021, 100, Rivista.Periodicita.MENSILE);
		archivio.aggiungiElemento(rivista1);

		logger.info(archivio.toString());

		// RIMOZIONE ELEMENTO DATO CODICE ISBN
		logger.info("RIMUOVI ELEMENTO");
		Libro libroDaRimuovere = new Libro(UUID.randomUUID(), "Cancellami", "Ajeje Brazolf", "Filosofia", 1990, 200);
		archivio.aggiungiElemento(libroDaRimuovere);
		logger.info(archivio.toString());
		UUID daRimuovere = libroDaRimuovere.getIsbn();
		archivio.rimuoviPerIsbn(daRimuovere);
		logger.info(archivio.toString());

		// RICERCA ELEMENTO DATO CODICE ISBN
		logger.info("CERCA ELEMENTO DATO ISBN");
		UUID daCercare = rivista1.getIsbn();
		OggettiLeggibili elementoTrovato = archivio.cercaPerIsbn(daCercare);
		if (elementoTrovato != null) {
			logger.info("Elemento trovato: " + elementoTrovato.toString() + "\n");
		} else {
			logger.info("Elemento non trovato" + "\n");
		}

		// CERCA ELEMENTO DATO ANNO PUBBLICAZIONE
		logger.info("CERCA ELEMENTO DATO ANNO PUBBLICAZIONE");
		List<OggettiLeggibili> oggettiPubblicatiNel2021 = archivio.cercaPerAnno(2021);
		logger.info("Elementi dell'archivio pubblicati nel 2021:");
		oggettiPubblicatiNel2021.forEach(oggetto -> logger.info(oggetto.toString() + "\n"));

		// CERCA LIBRO DATO AUTORE
		logger.info("CERCA LIBRO DATO AUTORE");
		String autore = "Umberto Eco";
		List<Libro> libriDiEco = archivio.cercaPerAutore(autore);
		logger.info("Libri di " + autore + ":");
		libriDiEco.forEach(oggetto -> logger.info(oggetto.toString() + "\n"));

		// SALVA E CARICA DA FILE
		logger.info("SALVA E CARICA DA FILE");
		try {
			archivio.salvaSuFile();
			logger.info("Archivio salvato su file.");
		} catch (IOException e) {
			logger.error("Errore durante il salvataggio su file: " + e.getMessage());
		}

		try {
			archivio.caricaDaFile();
			logger.info("Archivio caricato da file.");
		} catch (IOException e) {
			logger.error("Errore durante il caricamento da file: " + e.getMessage());
		}

		logger.info(archivio.toString());

	}

}
