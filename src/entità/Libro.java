package entità;

import java.util.UUID;

public class Libro extends OggettiLeggibili {
	private String autore;
	private String genere;

	public Libro(UUID isbn, String titolo, String autore, String genere, int annoPubblicazione, int numeroPagine) {
		super(isbn, titolo, annoPubblicazione, numeroPagine);
		this.autore = autore;
		this.genere = genere;
	}

	public String getAutore() {
		return autore;
	}

	public String getGenere() {
		return genere;
	}

	@Override
	public String toString() {
		return super.toString() + " - Autore: " + autore + " - Genere: " + genere;
	}

}